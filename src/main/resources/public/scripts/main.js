const code = document.querySelector("copy");
let content = document.getElementById("article");
let content_tag = document.getElementsByClassName("resault-tag");

code.onclick = function() {
  document.execCommand("copy");
}

code.addEventListener("copy", function(event) {
  event.preventDefault();
  if (event.clipboardData) {
    event.clipboardData.setData("text/plain", code.textContent);
    swal({
	icon:'success',
	title:'E-mail已複製',
	text:event.clipboardData.getData("text"),
	timer:1000,
	button: [],
        });
  }
});
if(content){
  content.innerHTML = content.innerText;
}
if(content_tag.length){
  $(content_tag).each((no,cont) => {
    cont.innerHTML = cont.innerText;
  });
}
const tag_map = tag =>{
  const tags = {
    '<copyborad>':`<div class="copyborad" xxx>
<button type="button" class="copybtn" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-original-title="複製" xxx>Copy</button xxx>

</div xxx>`,
    '<code-js>':`<pre class="scrollbar" xxx>
<code class="lang-js line-numbers" xxx>

</code xxx>
</pre xxx>`,
    '<code-java>':`<pre class="scrollbar" xxx>
<code class="lang-java line-numbers" xxx>

</code xxx>
</pre xxx>`,
    '<code-html>':`<pre class="scrollbar" xxx>
<code class="lang-html line-numbers" xxx>

</code xxx>
</pre xxx>`,
    '<code-css>':`<pre class="scrollbar" xxx>
<code class="lang-css line-numbers" xxx>

</code xxx>
</pre xxx>`,
    '<kbd>':`<kbd xxx>

</kbd xxx>`,
    '<var>':`<var xxx>

</var xxx>`,
    '<samp>':`<samp xxx>

</samp xxx>`,
    '<details>':`<details xxx>
<summary xxx>

</summary xxx>
<p xxx>

</p xxx>
</details xxx>`,
    '<img>':`<img src="" xxx>`,
    '<a>':`<a href="" xxx></a xxx>`
  };
  return tags[tag];
}

const replace = (s) =>{
  return s.replace(/</g,'&lt;').replace(/>/g,'&gt;');
}

const exe = (s) =>{
  return s.replace(/>/g,' xxx>');
}

let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
  return new bootstrap.Tooltip(tooltipTriggerEl)
});

$(document).ready(()=>{

  $('samp[class^=dropdown-item]').click((t)=>{
    let cursorPos = $('textarea').prop('selectionStart');
    let v = $('textarea').val();
    let textBefore = v.substring(0,  cursorPos);
    let textAfter  = v.substring(cursorPos, v.length);
    
    $('textarea').val(textBefore+'\n'+tag_map(t.target.innerText)+'\n'+textAfter);
    
  });
  
  $('samp[class=exec]').click((t)=>{
    let cursorPos = $('textarea').prop('selectionStart'),
        v = $('textarea').val(),
        textBefore = v.substring(0,  cursorPos),
        selection = window.getSelection().toString();
    let textAfter  = v.substring(textBefore.length+selection.length);

    if(t.target.innerText=='&lt;&gt;'){
      $('textarea').val(textBefore+replace(selection)+textAfter);
    return;
    }

    if(t.target.innerText=='標註執行'){
      $('textarea').val(textBefore+exe(selection)+textAfter);
    }
  });

  $('.copybtn').mouseover((e)=>{
    
    $(e.target).attr('data-bs-original-title', '複製').tooltip('show');

  });

  $('.copybtn').click((c)=>{
    
    let copybtn = c.target;
    let target_node = copybtn.parentNode;
    let new_text = target_node.innerText.substring(5);
    
     $('<input>', {
       id:'copy_content',
       type:'text',
       value:new_text,
       style:'display:none'
       }).appendTo(target_node);

    let node = $('#copy_content')[0];

    node.select();
    node.setSelectionRange(0, 99999); 
    navigator.clipboard.writeText(node.value);
    
    $('#copy_content').remove();
    window.setTimeout(()=>{$(copybtn).attr('data-bs-original-title', '已複製').tooltip('show')},400);
     window.setTimeout(()=>{
       $(copybtn).tooltip('hide')
     },1000);
    
  });
  
  $('.insert-tag').click((t)=>{
    let com = '';
    let tag = t.target.innerText;
    let input = $(t.target).parents('.form-label').next().val();
    if(input!=''){
        com = ',';
    }
    $(t.target).parents('.form-label').next().val(input+com+tag);
  });
  
  $('.dishidden').click(()=>{
	$('.hidden').css('display','inline-flex');
});

  $('input[name="file"]').change(()=>{
    const file = $('input[name="file"]').prop('files');
    if(file.length){
	
    let fs = $(file).map((index,arr)=>arr.size);
    let fileSize = 0;
	for(let i = 0;i<fs.length;i++){
		fileSize += fs[i];
	}
      if (Math.abs(fileSize) > 1153433600){
	    $('input[name="file"]').val("");
        $('.file_size').text('');
        return $('.file_name').text("檔案超過1G，無法上傳");
      }
    let fn = $(file).map((index,arr)=>arr.name);
    let fileName = "",comma = "";
	for(let i = 0;i<fn.length;i++){
		fileName += comma+fn[i];
		comma = "、 ";
	}
    $('.file_name').text("已選擇："+fileName);
      if (Math.abs(fileSize) < 1024) {
        return $('.file_size').text('檔案大小：'+fileSize + ' B');
      }

    let u = -1;
	
    const units = ['kB', 'MB', 'GB'];

      do{
	    fileSize /= 1024;
        ++u;
      }while((Math.round(Math.abs(fileSize) * 1000)) / 1000 >= 1024 && u < units.length - 1);
      $('.file_size').text('檔案大小：'+fileSize.toFixed(1) + ' ' + units[u]);
   }
  })

});
