function Captcha(id, testId, capchaUtilityDomElement, captchaTestElement, mxCaptchaAgentUrl) {
    this.id = id
    this.mxCaptchaAgentUrl = mxCaptchaAgentUrl,
        this.testIsOpen = false
    this.testId = testId
    this.testRequest
    this.captchaTestElement = captchaTestElement
    this.capchaUtilityDomElement = capchaUtilityDomElement
    this.userResponseTestInterfaceInput = this.captchaTestElement.querySelector(".cpch-container-style .test-container .test-container .test-response")
    this.captchaSubContainer = this.capchaUtilityDomElement.querySelector(".captcha-sub-container")
    this.checkBoxButton = this.capchaUtilityDomElement.querySelector("div.captcha-sub-container > div > .checkbox");
    this.verifyButton = this.captchaTestElement.querySelector("div.test-control-container  button[name='verify']")
    this.reloadButton = this.captchaTestElement.querySelector("div.test-control-container  button[name='reload']")
    this.statusSign = this.capchaUtilityDomElement.querySelector(".captcha-sub-container .status-sign")
    this.testInterfaceMessage = captchaTestElement.querySelector(".captcha-qst div.test-container > div.test-interface-message")
    this.testInterfaceMessageImg = captchaTestElement.querySelector(".captcha-qst div.test-container > div.test-container > div.test-imag-container > img")
    this.testInterfaceMessageImgLabel = captchaTestElement.querySelector(".captcha-qst div.test-container > div.test-container > div.test-imag-container p")


    this.httpClient = {
        xhr: function () {
            let xhr = new XMLHttpRequest()
            xhr.withCredentials = true
            return xhr
        }(),
        testRequest: null,
        test: null,
        captchabject: this,
        startTestHttp: function () {
            if (this.testRequest == null
                || !this.testRequest.hasOwnProperty("hostIdentifier")
                || !this.testRequest.hasOwnProperty("requestToken")) throw "Please, set a valid test request object"
            const path = "/captcha/agent/test.start"
            this.xhr.onreadystatechange = () => {
                if (this.xhr.readyState == 4 && this.xhr.status == 200) {
                    this.test = JSON.parse(this.xhr.responseText)
                    this.captchabject.updateCaptchaTestInterface(this.test, ()=>{
                        this.captchabject.testInterfaceMessage.innerHTML = ""
                    });
                }
                else if (this.xhr.readyState == 4 && this.xhr.status == 503) {
                    console.log("unavailale")
                }
                else if (this.xhr.readyState == 4 && this.xhr.status == 400) {

                }
                else {

                }
            }
            this.xhr.open("POST", this.captchabject.mxCaptchaAgentUrl + path)
            this.xhr.setRequestHeader("Content-Type", "application/json");
            this.xhr.send(JSON.stringify(this.testRequest))
        },
        reloadTestHttp: function () {
            const path = "/captcha/agent/test.qst.reset"
            this.xhr.onreadystatechange = () => {
                if (this.xhr.readyState == 4 && this.xhr.status == 200) {
                    this.test = JSON.parse(this.xhr.responseText)
                    this.captchabject.updateCaptchaTestInterface(this.test, ()=>{
                        this.captchabject.testInterfaceMessage.innerHTML = ""
                    });
                }
                else if (this.xhr.readyState == 4 && this.xhr.status == 503) {
                    console.log("unavailale")
                }
                else if (this.xhr.readyState == 4 && this.xhr.status == 400) {

                }
                else {

                }
            }
            this.xhr.open("GET", this.captchabject.mxCaptchaAgentUrl + path)
            this.xhr.setRequestHeader("Content-Type", "application/json");
            this.xhr.send(JSON.stringify(this.testRequest))
        },
        responseToTestHttp: function(userResponse){
            const path = "/captcha/agent/test.qst.response?response="
            this.xhr.onreadystatechange = () => {
                if (this.xhr.readyState == 4 && this.xhr.status == 200) {
                    let reponse = JSON.parse(this.xhr.responseText)
                    if (this.containHashToken(reponse)) {
                        this.captchabject.closeTest()
                        this.captchabject.setCaptchaSpinner()
                        console.log(reponse.hashedToken);
                        setTimeout(this.captchabject.setCaptchaVerified, 1000)
                    }else{
                        this.captchabject.updateCaptchaTestInterface(reponse, ()=>{
                            this.captchabject.setTestInterMessageError("Wrong response, Try again !")
                        });
                        
                    }
                }
                else if (this.xhr.readyState == 4 && this.xhr.status == 503) {
                    console.log("unavailale")
                }
                else if (this.xhr.readyState == 4 && this.xhr.status == 400) {
    
                }
                else {
                    console.log(this.xhr.responseText)
                }
                
            }
            this.xhr.open("GET", this.captchabject.mxCaptchaAgentUrl + path+userResponse)
            this.xhr.setRequestHeader("Content-Type", "application/json");
            this.xhr.send()
        },
        containHashToken:(reponse)=>{
            return reponse.hasOwnProperty("hashedToken")
        }
    }

    this.updateCaptchaTestInterface = function (test, captchaInterfaceMessageCallback) {
        const path = "/captcha/agent/test.image?name="
        this.testInterfaceMessageImg.src = this.mxCaptchaAgentUrl + path + test.imageName
        let loadedCallback = function () {
            this.testInterfaceMessageImgLabel.innerText = test.imageLabel
            this.testInterfaceMessageImg.style.visibility = "visible"
            captchaInterfaceMessageCallback()
            this.enableTestInterfaceButton()
        }
        loadedCallback = loadedCallback.bind(this)
        this.testInterfaceMessageImg.addEventListener('load', loadedCallback)

    }

    this.setTestRequest = (testRequest) => {
        this.httpClient.testRequest = testRequest
    }

    this.openTest = () => {
        if (!this.testIsOpen) {
            this.checkBoxButton.disabled = true
            this.checkBoxButton.classList.remove("active-checkbox")
            this.capchaUtilityDomElement.appendChild(this.captchaTestElement)
            this.captchaTestElement.classList.add("show")
            this.testIsOpen = true
            this.httpClient.startTestHttp()
            this.setTestInterfaceLoading();
            this.disableTestInterfaceButton();
        }

    }
    this.setErrorMessageInCaptcha = (message) => {
        this.captchaSubContainer.innerHTML = '<p class="errorCaptcha">' + message + '</p>'
    }
    this.disableTestInterfaceButton = () => {
        this.verifyButton.disabled = true
        this.verifyButton.classList.remove("button-active")
        this.reloadButton.disabled = true
        this.reloadButton.classList.remove("button-active")
    }
    this.enableTestInterfaceButton = () => {
        this.verifyButton.disabled = false
        this.verifyButton.classList.add("button-active")
        this.reloadButton.disabled = false
        this.reloadButton.classList.add("button-active")
    }
    this.closeTest = () => {
        if (this.testIsOpen) {
            this.captchaTestElement.classList.remove("show")
            this.captchaTestElement.innerHTML = ""
            testIsOpen = false
        }
    }
    this.setCaptchaVerified = () => {
        this.statusSign.innerHTML = '<img class="check-mark" src="./checkmark.png">'
        this.statusSign.firstChild.classList.add("show")
    }
    this.setCaptchaSpinner = () => {
        this.statusSign.innerHTML = '<span class="loader cp-finishing"></span>'
    }
    this.verifyHandler = () => {
        let userResponse = this.userResponseTestInterfaceInput.value
        if (userResponse.length == 0 ){
            this.setTestInterMessageError("Please enter the answer !")
        }
        else {
            this.httpClient.responseToTestHttp(userResponse)
            this.setTestInterfaceLoading();
            this.disableTestInterfaceButton();
        }
    }


    this.reloadHandler = () => {
        this.setTestInterfaceLoading();
        this.disableTestInterfaceButton();
        this.hideTestImageAndLabel()
        this.httpClient.reloadTestHttp()
    }
    this.setTestInterfaceLoading = () => {
        this.testInterfaceMessage.innerHTML = '<span class="dot-loader"></span>'
    }
    this.setTestInterMessageError = (message) => {
        this.testInterfaceMessage.innerHTML = '<p class="error-message">' + message + '</p>'
    }

    this.hideTestImageAndLabel = () => {
        this.testInterfaceMessageImgLabel.innerText = ""
        this.testInterfaceMessageImg.style.visibility = "hidden"
    }

    this.checkBoxButton.onclick = this.openTest
    this.verifyButton.onclick = this.verifyHandler
    this.reloadButton.onclick = this.reloadHandler
}


const CaptchaFactory = (function () {
    var instance;
    var lastCaptchaId = 0;
    var domParser = new DOMParser();
    var idPreFix = "mx-captcha#"
    var captchaUtiliy = '<div class="captcha-utility">' +
        '            <div class="captcha-container cpch-container-style">' +
        '                <div class="captcha-sub-container">' +
        '                    <div class="status-sign">' +
        '                        <button type="button" name="check-box" class="checkbox active-checkbox"' +
        '                            title="captcha starter"></button>' +
        '                    </div>' +
        '                    <p>I am not a robot</p>' +
        '                </div>' +
        '                <div class="captcha-logo">' +
        '                    <img src="captcha-image.png" alt="Captcha">' +
        '                    <p>CapTcHA</p>' +
        '                </div>' +
        '            </div>' +
        '        </div>'
    var captchaTestInterface = '<div class="cpch-container-style captcha-qst">' +
        '                <span class="arrow"></span>' +
        '                <div class="test-container">' +
        '                    <div class="test-title">' +
        '                        <p>Solve the captcha puzzle below</p>' +
        '                        <p>Click verify when you are done</p>' +
        '                    </div>' +
        '                    <div class="test-container">' +
        '                        <div class="test-imag-container">' +
        '                            <p></p>' +
        '                            <img src="" style="visibility:hidden">' +
        '                        </div>' +
        '                        <div class="test-interface-message">' +
        '                        </div>' +
        '                        <input type="text" class="test-response">' +
        '                    </div>' +
        '                </div>' +
        '                <div class="test-control-container">' +
        '                    <button type="button" name="reload" class="button button-active button-with-icon" title="captcha reload"><img' +
        '                            src="rotation.png" alt="reload"></button>' +
        '                    <button type="button" name="verify" class="button button-active blue-button"' +
        '                        title="captcha verify">Verify</button>' +
        '                </div>' +
        '            </div>'
    function createInstance(mxCaptchaAgentUrl) {
        return {
            createCaptcha: function (domContainer, testId) {
                lastCaptchaId++
                let captchaDom = domParser.parseFromString(captchaUtiliy, "text/html").body.firstChild;
                let captchaTestInterfaceDom = domParser.parseFromString(captchaTestInterface, "text/html").body.firstChild;
                captchaDom.id = idPreFix + lastCaptchaId
                domContainer.innerHTML = ""
                domContainer.appendChild(captchaDom)
                return new Captcha(lastCaptchaId, testId, captchaDom, captchaTestInterfaceDom, mxCaptchaAgentUrl)
            }
        }
    }
    return {
        getInstance: function (mxCaptchaAgentUrl) {
            if (instance == null) {
                instance = createInstance(mxCaptchaAgentUrl)
            }
            return instance
        }
    }
})();