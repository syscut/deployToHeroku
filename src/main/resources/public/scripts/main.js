const code = document.querySelector("copy");
let content = document.getElementById("article");

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
const tag_map = tag =>{
  const tags = {
    '<copyborad>':'<div class="copyborad">\n<button type="button" class="copybtn" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-original-title="複製程式碼">Copy</button><div class="mt-30">\n\n</div></div>',
    '<code-js>':'<pre class="scrollbar"><code class="scrollbar lang-js line-numbers">\n\n</code></pre>',
    '<code-java>':'<pre class="scrollbar"><code class="scrollbar lang-java line-numbers">\n\n</code></pre>',
    '<code-html>':'<pre class="scrollbar"><code class="scrollbar lang-html line-numbers">\n\n</code></pre>',
    '<code-css>':'<pre class="scrollbar"><code class="scrollbar lang-css line-numbers">\n\n</code></pre>',
    '<kbd>':'<kbd>\n\n</kbd>',
    '<var>':'<var>\n\n</var>',
    '<samp>':'<samp>\n\n</samp>',
    '<details>':'<details>\n<summary>\n\n</summary>\n<p>\n\n</p>\n</details>',
    '<img>':'<img src="">',
    '&lt;&gt;':'&lt; &gt;'
  };
  return tags[tag];
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

  $('.copybtn').click((c)=>{
    const range = document.createRange();
    let copybtn = c.target;
    range.selectNode(copybtn.nextSibling);
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
