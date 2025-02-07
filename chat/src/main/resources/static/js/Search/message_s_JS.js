let respondclass;
let i=0;
let rid;
let respondid;
let drs;
let membername;
let memberphoto;
function pass1(value) {
    respondid ='#' + value;
}
function pass2(value) {
    respondclass = value;
}
function passmembername(pmembername){
		membername=pmembername;
}
function passmemberphoto(pmemberphoto){
		memberphoto=pmemberphoto;
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
        //y=calTime + '秒';
		y='剛剛';
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
function userRespond() {
	console.log(membername);
    let a = $('.sendmsg').val();
    if (a != "") {
        if (a.match(/@\w+/g)) {
            a = mTag(a);
        }
        if (respondclass === 'pr') {
            drs='dfrespond';
            rid='rs'+i++;
            respondid='.postContent';
        } else{
            drs='dcrespond';
            rid=respondid.replace('#','');
        }
        let x = new Date(); //時間要改
		if(respondid==='.postContent'){
        	$('.postStreamBox').append(`
			<article id=${rid} iddata=${rid} class=${drs}>
				<div class="memberphotospace">
					<img class="memberphoto" src="data:image/png;base64,${memberphoto}">
				</div>
			    <section class="username">${membername}</section>
			    <section class="memberrespond">${a}</section>
			    <div class="love">♡</div>
			    <section class="myTime" id="timeslip" timeSet="${x}">剛剛</section>
			    <section class="respond">回覆</section>
			</article>
			`);
			}else{
				$(`${respondid}`).after(`
			    <article iddata="${rid}" class=${drs}>
			    	<div class="memberphotospace">
						<img class="memberphoto" src="data:image/png;base64,${memberphoto}">
					</div>
			        <section class="username">${membername}</section>
			        <section class="memberrespond">${a}</section>
			        <div class="love">♡</div>
			        <section class="myTime" id="timeslip" timeSet="${x}">剛剛</section>
			        <section class="respond">回覆</section>
			    </article>
			`);}
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
        let linkHTML = `<a class="ra" href="${url}">${keyword}</a>`;
        let regex = new RegExp(`${keyword}`, 'g');  // 使用正則表達式來匹配整詞
        mr = mr.replace(regex, linkHTML);
    }
    return mr;
}

function saveboarddata(){
	let data = [];
	let pId = $('.post').attr('id');
	data.push({
		"postId":pId
	});
	$(".dfrespond, .dcrespond").each(function() {
		let className = $(this).attr("class");  // 取得 class
		let iddata = $(this).attr("iddata");   // 取得 iddata 屬性
		let photo = $(this).find(".memberphoto").attr("src"); //抓圖
		// 這裡抓取子元素的 class 名稱和內容
		let childElements = $(this).find(".username, .memberrespond").map(function() {
			return {
				"class": $(this).attr("class"),  // 取得class名稱
				"content": $(this).text().trim()  // 取得內容並去除空格
			};
		}).get();  // 轉成陣列
		let time = $(this).find(".myTime").attr("timeSet");
					
		data.push({
			"class": className,
			"iddata": iddata,
			"memberphoto": photo,
			"childElements": childElements,
			"myTime": time  
		});
	});
	$.ajax({
		url: `/saveData/savePostData/${pId}`,  // 後端 API 端點
		type: "POST",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(data),  // 轉 JSON 字串
	});	
}