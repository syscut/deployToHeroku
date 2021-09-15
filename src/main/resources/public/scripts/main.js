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
<div class="mt-30" xxx>

</div xxx>
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

  $('.copybtn').click((c)=>{
    const range = document.createRange();
    let copybtn = c.target;
    range.selectNode(copybtn.nextElementSibling);
    const selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
    document.execCommand('copy');
    selection.removeAllRanges();
    
    window.setTimeout(()=>{$(copybtn).attr('data-bs-original-title', '已複製').tooltip('show')},400);
    window.setTimeout(()=>{
      $(copybtn).tooltip('hide').attr('data-bs-original-title', '複製程式碼')
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
});
