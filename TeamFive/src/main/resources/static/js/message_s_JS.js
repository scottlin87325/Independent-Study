let rType;
let i=0;
let rid;
let idsave;
let drs;
function pass1(value) {
    idsave ='#' + value;
}
function pass2(value) {
    rType = value;
}

function settime(){
    $('.myTime').each(function() {
        // 獲取每個 <td> 的起始秒數
        var startTime = new Date($(this).attr('timeSet'));
        var save = startTime;  // 設定當前時間為起始秒數
        let y = timeSlip(save);
        // console.log(y);
        
        $(this).text(y);
    });
}

//發文or留言經過時間計算
function timeSlip(inputTime) {  //inputTime輸入時間要抓資料庫
    let currentTime = new Date();
    let y;
    calTime = Math.round((currentTime - inputTime) / 1000);
    if (calTime < 60) {
        // console.log(calTime + '秒');
        y=calTime + '秒';
    } else if (calTime < 3600) {
        let x = Math.ceil(calTime / 60);
        // console.log(x + '分');
        y=x + '分'
    } else if (calTime < 86400) {
        let x = Math.floor(calTime / 3600);
        // console.log(x + '小時');
        y=x + '小時';
    } else if (calTime < 2592000) {
        let x = Math.floor(calTime / 86400);
        // console.log(x + '天');
        y=x + '天';
    } else {
        let x = Math.floor(calTime / 604800);
        // console.log(x + '週');
        y=x + '週';
    }
    return y;
}

//用戶留言區塊
function userRespond(userName) {
    let rs;
    let rst;
    let a = $('#mytext').val();
    if (a != "") {
        if (a.match(/@\w+/g)) {
            a = mTag(a);
        }
        if (rType === 'post') {
            drs='dfrespond';
            rid='id=rs'+i++;
            idsave='.divRight';
        } else if (rType === 'dcrespond' || rType === 'dfrespond') {
            drs='dcrespond';
            rid='';
        }
        let x = new Date(); //時間要改
        
        $(`${idsave}`).append(`
        <div ${rid} class="${drs}">
        <table style="width: 100%;">
            <tr>
                <td class="username">${userName}</td>
                <td> : ${a}</td>
                <td class="love" style="text-align: right; font-size: 25px;">♡</td>
            </tr>
            <tr>
                <td class="myTime" timeSet='${x}'></td>
                <td class="respond">回覆</td>
                <td style="width: 80%"></td>
            </tr>
        </table>
        </div>`);
    }
    $('#mytext').val('');
}

function mTag(mr) {
    
    let links={};
    let matches = mr.match(/@\w+/g);
    for (let i=0;i<matches.length ;i++) {
        links[matches[i]]= "https://google.com";
    }
    for (let [keyword, url] of Object.entries(links)) {
        let linkHTML = `<a href="${url}">${keyword}</a>`;
        let regex = new RegExp(`${keyword}`, 'g');  // 使用正則表達式來匹配整詞
        mr = mr.replace(regex, linkHTML);
    }
    return mr;
}