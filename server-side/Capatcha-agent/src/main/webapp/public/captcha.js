HttpClient = function(captchaObject) {
    this.xhr =  new XMLHttpRequest()
    this.xhr.withCredentials = true
    this.testRequest = null
    this.test = null
    this.captchaObject = captchaObject
    this.startTestHttp =  ()=>{
        if (this.testRequest === null
            || !this.testRequest.hasOwnProperty("hostIdentifier")
            || !this.testRequest.hasOwnProperty("requestToken")) throw "Please, set a valid test request object"
        const path = "/captcha/agent/test.start"
        this.xhr.onreadystatechange = () => {
            if (this.xhr.readyState === 4 && this.xhr.status === 200) {
                this.test = JSON.parse(this.xhr.responseText)
                this.captchaObject.updateCaptchaTestInterface(this.test, this.captchaObject.closeTestInterfaceLoader);
            } else if (this.xhr.readyState === 4 && this.xhr.status === 503) {
                console.log("unavailale")
            } else if (this.xhr.readyState === 4 && this.xhr.status === 400) {

            } else {

            }
        }
        this.xhr.open("POST", this.captchaObject.mxCaptchaAgentUrl + path)
        this.xhr.setRequestHeader("Content-Type", "application/json");
        this.xhr.send(JSON.stringify(this.testRequest))
    }
    this.reloadTestHttp = () => {
        const path = "/captcha/agent/test.qst.reset"
        this.xhr.onreadystatechange = () => {
            if (this.xhr.readyState === 4 && this.xhr.status === 200) {
                this.test = JSON.parse(this.xhr.responseText)
                this.captchaObject.updateCaptchaTestInterface(this.test, () => {
                    this.captchaObject.setTestInterfaceLoading()
                    setTimeout(this.captchaObject.closeTestInterfaceLoader, 900)
                });
            } else if (this.xhr.readyState === 4 && this.xhr.status === 503) {
                console.log("unavailale")
            } else if (this.xhr.readyState === 4 && this.xhr.status === 400) {
                console.log(this.xhr.responseText)
            } else {

            }
        }
        this.xhr.open("GET", this.captchaObject.mxCaptchaAgentUrl + path)
        this.xhr.setRequestHeader("Content-Type", "application/json");
        this.xhr.send(JSON.stringify(this.testRequest))
    }
    this.responseToTestHttp = (userResponse) => {
        const path = "/captcha/agent/test.qst.response?response="
        this.xhr.onreadystatechange = () => {
            if (this.xhr.readyState === 4 && this.xhr.status === 200) {
                let response = JSON.parse(this.xhr.responseText)
                if (this.containHashToken(response)) {
                    this.captchaObject.closeTest()
                    this.captchaObject.setCaptchaSpinner()
                    let input = document.createElement("input");
                    input.setAttribute("name", "mxCaptchaHashedToken")
                    input.setAttribute("value", response["hashedToken"])
                    input.setAttribute("label", "Captcha hashed token")
                    input.style.display = "none"
                    if (this.captchaObject.onCaptchaVerifiedCallback !== null) this.captchaObject.onCaptchaVerifiedCallback()
                    this.captchaObject.capchaUtilityDomElement.appendChild(input)
                    setTimeout(this.captchaObject.setCaptchaVerified, 1000)
                } else {
                    this.captchaObject.updateCaptchaTestInterface(response, () => {
                        this.captchaObject.setTestInterMessageError("Wrong response, Try again !")
                    });
                    setTimeout(() => {
                        this.captchaObject.userResponseTestInterfaceInput.value = ""
                    }, 700)

                }
            } else if (this.xhr.readyState === 4 && this.xhr.status === 503) {
                console.log("unavailale")
            } else if (this.xhr.readyState === 4 && this.xhr.status === 400) {

            } else {
                console.log(this.xhr.responseText)
            }

        }
        this.xhr.open("GET", this.captchaObject.mxCaptchaAgentUrl + path + userResponse)
        this.xhr.setRequestHeader("Content-Type", "application/json");
        this.xhr.send()
    }
    this.containHashToken = (reponse) => {
        return reponse.hasOwnProperty("hashedToken")
    }
}
function Captcha(id, testId, captchaUtilityDomElement, captchaTestElement, mxCaptchaAgentUrl, onCaptchaVerifiedCallback) {
    this.mxCaptchaAgentUrl = mxCaptchaAgentUrl
    this.publicRessourcesURL = mxCaptchaAgentUrl + "/public"
    this.testIsOpen = false
    this.captchaTestElement = captchaTestElement
    this.capchaUtilityDomElement = captchaUtilityDomElement
    this.userResponseTestInterfaceInput = this.captchaTestElement.querySelector(".cpch-container-style .test-container .test-container .test-response")
    this.captchaSubContainer = this.capchaUtilityDomElement.querySelector(".captcha-sub-container")
    this.checkBoxButton = this.capchaUtilityDomElement.querySelector("div.captcha-sub-container > div > .checkbox");
    this.verifyButton = this.captchaTestElement.querySelector("div.test-control-container  button[name='verify']")
    this.reloadButton = this.captchaTestElement.querySelector("div.test-control-container  button[name='reload']")
    this.statusSign = this.capchaUtilityDomElement.querySelector(".captcha-sub-container .status-sign")
    this.testInterfaceMessage = captchaTestElement.querySelector(".captcha-qst div.test-container > div.test-interface-message")
    this.testInterfaceMessageImg = captchaTestElement.querySelector(".captcha-qst div.test-container > div.test-container > div.test-imag-container > img")
    this.testInterfaceMessageImgLabel = captchaTestElement.querySelector(".captcha-qst div.test-container > div.test-container > div.test-imag-container p")
    this.onCaptchaVerifiedCallback = onCaptchaVerifiedCallback
    this.httpClient = new HttpClient(this)

    this.updateCaptchaTestInterface = (test, captchaInterfaceMessageCallback) => {
        const path = "/captcha/agent/test.image?name="
        this.testInterfaceMessageImg.src = this.mxCaptchaAgentUrl + path + test["imageName"]
        let loadedCallback = (cap) => {
            cap.testInterfaceMessageImgLabel.innerText = test["imageLabel"]
            cap.testInterfaceMessageImg.style.visibility = "visible"
            captchaInterfaceMessageCallback()
            cap.enableTestInterfaceButton()
        }
        this.testInterfaceMessageImg.addEventListener('load', () => {
            loadedCallback(this)
        })

    }

    this.openTest = () => {
        if (!this.testIsOpen) {
            this.checkBoxButton.disabled = true
            this.checkBoxButton.classList.remove("active-checkbox")
            this.capchaUtilityDomElement.appendChild(this.captchaTestElement)
            this.captchaTestElement.classList.add("show")
            this.testIsOpen = true
            this.httpClient.startTestHttp()
            this.disableTestInterfaceButton();
        }

    }
    this.setErrorMessageInCaptcha = (message) => {
        this.captchaSubContainer.innerHTML = '<p class="errorCaptcha">' + message + '</p>'
    }
    this.closeTestInterfaceLoader = () => {
        this.testInterfaceMessage.innerHTML = ""
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
            this.testIsOpen = false
        }
    }
    this.setCaptchaVerified = () => {
        this.statusSign.innerHTML = '<img class="check-mark" src="' + this.publicRessourcesURL + '/checkmark.png" alt="checkmark">'
        this.statusSign.firstChild.classList.add("show")
    }
    this.setCaptchaSpinner = () => {
        this.statusSign.innerHTML = '<span class="loader cp-finishing"></span>'
    }
    this.verifyHandler = () => {
        let userResponse = this.userResponseTestInterfaceInput.value
        if (userResponse.length === 0) {
            this.setTestInterMessageError("Please enter the answer !")
            setTimeout(() => {
                this.setTestInterMessageError("")
            }, 1000)
        } else {
            this.httpClient.responseToTestHttp(userResponse)
            this.setTestInterfaceLoading();
            this.disableTestInterfaceButton();
        }
    }


    this.reloadHandler = () => {
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

    this.setTestRequest = (testRequest) => {
        this.httpClient.testRequest = testRequest
    }
}


const CaptchaFactory = (function () {
    var instance = null;
    var publicResourcesURL = null;
    var lastCaptchaId = 0;
    var domParser = new DOMParser();
    var idPreFix = "mx-captcha#"
    var captchaUtility = null
    var captchaTestInterface = null

    function createInstance(mxCaptchaAgentUrl) {
        return {
            createCaptcha: function (domContainer, testId, onCaptchaVerifiedCallback = null) {
                lastCaptchaId++
                let captchaDom = domParser.parseFromString(captchaUtility, "text/html").body.firstChild;
                let captchaTestInterfaceDom = domParser.parseFromString(captchaTestInterface, "text/html").body.firstChild;
                captchaDom.id = idPreFix + lastCaptchaId
                domContainer.innerHTML = ""
                domContainer.appendChild(captchaDom)
                return new Captcha(lastCaptchaId, testId, captchaDom, captchaTestInterfaceDom, mxCaptchaAgentUrl, onCaptchaVerifiedCallback)
            }
        }
    }


    return {
        getInstance: function (mxCaptchaAgentUrl) {
            if (instance === null) {
                publicResourcesURL = mxCaptchaAgentUrl + "/public"
                captchaUtility = '<div class="captcha-utility">' +
                    '            <div class="captcha-container cpch-container-style">' +
                    '                <div class="captcha-sub-container">' +
                    '                    <div class="status-sign">' +
                    '                        <button type="button" name="check-box" class="checkbox active-checkbox"' +
                    '                            title="captcha starter"></button>' +
                    '                    </div>' +
                    '                    <p>I am not a robot</p>' +
                    '                </div>' +
                    '                <div class="captcha-logo">' +
                    '                    <img src="' + publicResourcesURL + '/captcha-image.png" alt="Captcha">' +
                    '                    <p>CapTcHA</p>' +
                    '                </div>' +
                    '            </div>' +
                    '        </div>'
                captchaTestInterface = '<div class="cpch-container-style captcha-qst">' +
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
                    '                            src="' + publicResourcesURL + '/rotation.png" alt="reload"></button>' +
                    '                    <button type="button" name="verify" class="button button-active blue-button"' +
                    '                        title="captcha verify">Verify</button>' +
                    '                </div>' +
                    '            </div>'

                instance = createInstance(mxCaptchaAgentUrl)
                var head = document.getElementsByTagName('head')[0];
                var link = document.createElement('link');
                const Roboto = new FontFace('Roboto', 'url(' + publicResourcesURL + '/font/Roboto-Regular.ttf)');
                Roboto.load().then(function (loadedFont) {
                    document.fonts.add(loadedFont)
                })
                const IBMPlexSans = new FontFace('IBM Plex Sans', 'url(' + publicResourcesURL + '/font/IBMPlexSans-Bold.ttf)');
                IBMPlexSans.load().then(function (loadedFont) {
                    document.fonts.add(loadedFont)
                })
                link.rel = 'stylesheet';
                link.type = 'text/css';
                link.href = publicResourcesURL + '/captcha.css';
                link.media = 'all';
                head.appendChild(link);
            }
            return instance
        }
    }
})();