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
    '<copyborad>':`<div class="copyborad">
<button type="button" class="copybtn" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-original-title="複製">Copy</button>
<div class="mt-30">

</div>
</div>`,
    '<code-js>':`<pre class="scrollbar">
<code class="scrollbar lang-js line-numbers">
    
</code>
</pre>`,
    '<code-java>':`<pre class="scrollbar">
<code class="scrollbar lang-java line-numbers">
      
</code>
</pre>`,
    '<code-html>':`<pre class="scrollbar">
<code class="scrollbar lang-html line-numbers">

</code>
</pre>`,
    '<code-css>':`<pre class="scrollbar">
<code class="scrollbar lang-css line-numbers">

</code>
</pre>`,
    '<kbd>':`<kbd>

</kbd>`,
    '<var>':`<var>
    
</var>`,
    '<samp>':`<samp>
    
</samp>`,
    '<details>':`<details>
<summary>
      
</summary>
<p>
      
</p>
</details>`,
    '<img>':`<img src="">`,
    '&lt;&gt;':''
  };
  return tags[tag];
}

const replace = (s) =>{
  return s.replace(/</g,'&lt;').replace(/>/g,'&gt;');
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
    
    if(tag_map(t.target.innerText)==''){
      let selection = window.getSelection().toString();
      textAfter = textAfter.substring(0,(textAfter.length-selection.length));
      $('textarea').val(textBefore+replace(selection)+textAfter);
      return;
    }
    $('textarea').val(textBefore+'\n'+tag_map(t.target.innerText)+'\n'+textAfter);
    
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
  
});
