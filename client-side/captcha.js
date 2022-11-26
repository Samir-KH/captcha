function Captcha(){};

Captcha.prototype.openTest = ()=>{
    const test = document.querySelector(".capatcha-utility div.cpch-container-style.captcha-qst");
    //test.style.display = "block"
    if (!test.classList.contains("show"))
        test.classList.add("show")
}

Captcha.prototype.closeTest = ()=>{
    const test = document.querySelector(".capatcha-utility div.cpch-container-style.captcha-qst");
    //test.style.display = "block"
    if (test.classList.contains("show"))
        test.classList.remove("show")
}

const captcha = new Captcha();