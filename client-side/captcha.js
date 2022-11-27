function Captcha(id, testId, capchaUtilityDomElement, captchaTestElement) {
    this.id = id
    this.testIsOpen = false
    this.testId = testId
    this.captchaTestElement = captchaTestElement
    this.capchaUtilityDomElement = capchaUtilityDomElement
    this.captchaSubContainer = capchaUtilityDomElement.querySelector(".captcha-sub-container")
    this.checkBoxButton = this.capchaUtilityDomElement.querySelector("div.captcha-sub-container > div > .checkbox");
    this.verifyButton = this.captchaTestElement.querySelector("div.test-control-container  button.blue-button")
    this.statusSign = this.capchaUtilityDomElement.querySelector(".captcha-sub-container .status-sign")
    this.testInterfaceMessage = captchaTestElement.querySelector(".captcha-qst div.test-container > div.test-interface-message")
    this.openTest = () => {
        if (!this.testIsOpen) {
            this.capchaUtilityDomElement.appendChild(this.captchaTestElement)
            this.captchaTestElement.classList.add("show")
            testIsOpen = true
        }
        this.setTestInterfaceLoading();
        setTimeout(() => this.setTestInterMessageError("message"), 2000)
    }
    this.setErrorMessageInCaptcha = (message) => {
        this.captchaSubContainer.innerHTML = '<p class="errorCaptcha">' + message + '</p>'
    }
    this.closeTest = () => {
        if (this.testIsOpen) {
            this.captchaTestElement.classList.remove("show")
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
        this.closeTest()
        this.setCaptchaSpinner()
        setTimeout(this.setCaptchaVerified, 1000)
        //this.setErrorMessageInCaptcha("<span>&#9888;</span> service unreachable")
    }
    this.setTestInterfaceLoading = () => {
        this.testInterfaceMessage.innerHTML = '<span class="dot-loader"></span>'
    }
    this.setTestInterMessageError = (message) => {
        this.testInterfaceMessage.innerHTML = '<p class="error-message">' + message + '</p>'
    }
    this.checkBoxButton.onclick = this.openTest
    this.verifyButton.onclick = this.verifyHandler

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
        '                        <button type="button" name="check-box" class="checkbox"' +
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
        '                            <p>Type de code contained in this image</p>' +
        '                            <img src="./test.png">' +
        '                        </div>' +
        '                        <div class="test-interface-message">' +
        '                        </div>' +
        '                        <input type="text" class="test-response">' +
        '                    </div>' +
        '                </div>' +
        '                <div class="test-control-container">' +
        '                    <button type="button" name="reload" class="button button-with-icon" title="captcha reload"><img' +
        '                            src="rotation.png" alt="reload"></button>' +
        '                    <button type="button" name="verify" class="button blue-button"' +
        '                        title="captcha verify">Verify</button>' +
        '                </div>' +
        '            </div>'
    function createInstance() {
        return {
            createCaptcha: function (domContainer, testId) {
                lastCaptchaId++
                let captchaDom = domParser.parseFromString(captchaUtiliy, "text/html").body.firstChild;
                let captchaTestInterfaceDom = domParser.parseFromString(captchaTestInterface, "text/html").body.firstChild;
                captchaDom.id = idPreFix + lastCaptchaId
                domContainer.innerHTML = ""
                domContainer.appendChild(captchaDom)
                return Captcha(lastCaptchaId, testId, captchaDom, captchaTestInterfaceDom)
            }
        }
    }
    return {
        getInstance: function () {
            if (instance == null) {
                instance = createInstance()
            }
            return instance
        }
    }
})();