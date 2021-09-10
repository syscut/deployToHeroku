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
    '<copyborad>':'<div class="copyborad">\n<button type="button" class="copybtn">Copy</button>\n\n</div>',
    '<code>':'<code>\n\n</code>',
    '<kbd>':'<kbd>\n\n</kbd>',
    '<var>':'<var>\n\n</var>',
    '<samp>':'<samp>\n\n</samp>',
    '<details>':'<details>\n<summary>\n\n</summary>\n<p>\n\n</p>\n</details>',
    '<img>':'<img src="">',
    '&lt;&gt;':'&lt; &gt;'
  };
  return tags[tag];
}
$(document).ready(()=>{
  $('samp[class^=dropdown-item]').click((t)=>{
    let cursorPos = $('textarea').prop('selectionStart');
    let v = $('textarea').val();
    let textBefore = v.substring(0,  cursorPos);
    let textAfter  = v.substring(cursorPos, v.length);

    $('textarea').val(textBefore+'\n'+tag_map(t.target.innerText)+'\n'+textAfter);
    
  })
});
