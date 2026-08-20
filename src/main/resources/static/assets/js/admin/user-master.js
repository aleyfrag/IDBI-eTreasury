/** Initializes the hardcoded Chrome-style tabs on the Admin User Master page. */
function initializeAdminUserMasterTabs() {
  const workspace = document.querySelector("[data-admin-tabs]");
  if (!workspace) return;

  const tabs = [...workspace.querySelectorAll("[data-admin-tab]")];
  const panels = [...workspace.querySelectorAll("[data-admin-panel]")];

  /** Selects one tab and shows only its matching content panel. */
  const activate = (tabId, moveFocus = false) => {
    tabs.forEach((tab) => {
      const selected = tab.dataset.adminTab === tabId;
      tab.classList.toggle("active", selected);
      tab.setAttribute("aria-selected", String(selected));
      tab.setAttribute("tabindex", selected ? "0" : "-1");
      if (selected && moveFocus) tab.focus();
    });

    panels.forEach((panel) => {
      panel.classList.toggle("d-none", panel.dataset.adminPanel !== tabId);
    });
  };

  /** Clicking a tab toggles the visible Admin feature without reloading the page. */
  tabs.forEach((tab, index) => {
    tab.addEventListener("click", () => activate(tab.dataset.adminTab));

    // Arrow keys provide familiar keyboard navigation between browser-like tabs.
    tab.addEventListener("keydown", (event) => {
      if (event.key !== "ArrowLeft" && event.key !== "ArrowRight") return;
      event.preventDefault();
      const direction = event.key === "ArrowRight" ? 1 : -1;
      const nextIndex = (index + direction + tabs.length) % tabs.length;
      activate(tabs[nextIndex].dataset.adminTab, true);
    });
  });

  // The first hardcoded entry is the default tab.
  if (tabs.length) activate(tabs[0].dataset.adminTab);
}
function initializeUserManagement(){
 const form=document.querySelector("[data-user-master-form]");if(!form)return;
 const ein=form.querySelector("[data-user-lookup-input]"),msg=form.querySelector("[data-user-master-message]");let timer;
 const show=(text,error=false,reload=false)=>{clearTimeout(timer);msg.textContent=text;msg.className=`user-master-message ${error?"is-error":"is-success"}`;timer=setTimeout(()=>{msg.classList.add("is-fading");setTimeout(()=>{msg.classList.add("d-none");if(reload)location.reload();},650);},15000);};
 const lookup=async()=>{if(!ein.value.trim())return;try{const r=await fetch(`${form.action.replace(/\/action$/,"/lookup")}?value=${encodeURIComponent(ein.value.trim())}`);const b=await r.json();if(!r.ok||!b.found)throw new Error(b.message||"User not found in e-Treasury or HRMS");const u=b.user;form.elements.einNumber.value=u.einNumber||ein.value.trim();form.elements.userCode.value=u.userCode||ein.value.trim();form.elements.solId.value=u.solId||"";form.elements.status.value=u.status||"A";form.elements.admin.checked=String(u.rights||"").toUpperCase()==="ADM";show(u.role?"Registered user loaded.":"HRMS employee found. Complete details and save.");}catch(e){show(e.message,true);}};
 ein.addEventListener("blur",lookup);
 form.addEventListener("submit",async e=>{e.preventDefault();const action=e.submitter?.value;if(!action)return;if(["DELETE","REJECT"].includes(action)&&!confirm(`${action} this user?`))return;const data=new FormData(form);data.set("action",action);try{const r=await fetch(form.action,{method:"POST",body:data});const b=await r.json();if(!r.ok)throw new Error(b.message||`${action} failed`);show(b.message||`${action} completed.`,false,true);}catch(x){show(x.message,true);}});
 form.addEventListener("reset",()=>{clearTimeout(timer);msg.classList.add("d-none");});
 document.querySelectorAll("[data-user-row]").forEach(row=>row.addEventListener("click",()=>{ein.value=row.dataset.search;lookup();}));
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded",()=>{initializeAdminUserMasterTabs();initializeUserManagement();});
} else {
  initializeAdminUserMasterTabs();
  initializeUserManagement();
}
