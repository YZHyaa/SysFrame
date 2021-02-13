/**
 * Created by shunchen_yang on 16/10/25.
 */
UE.registerUI('dialog', function (editor, uiName) {
    var btn = new UE.ui.Button({
        name: 'xiumi-connect',
        title: '秀米',
        onclick: function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl: '/plugins/ueditor/xiumi/xiumi-ue-dialog-v5.html',
                editor: editor,
                name: 'xiumi-connect',
                //title: "秀米图文消息助手",
                cssRules: "width: " + (window.innerWidth - 60) + "px;" + "height: " + (window.innerHeight - 60) + "px;"
                //如果给出了buttons就代表dialog有确定和取消
                /*buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '关闭',
                        onclick: function () {
                            dialog.close(true);
                        }
                    }
                ]*/
            });
            dialog.render();
            dialog.open();
        }
    });

    return btn;
});