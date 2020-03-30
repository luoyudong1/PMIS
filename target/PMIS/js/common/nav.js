/**
 * Created by YFZX-WB on 2017/3/31.
 */
define(['jquery'], function($) {

    return {    	
        openTab: function (tab) {
            $('#side-menu a[href="#fun_' + tab + '"]').tab('show');
        },
        closeTab: function (tab) {
            if ($('#tab_' + tab).hasClass('active')) {
                $("#tab_" + tab).prev().addClass('active');
                $("#" + tab).prev().addClass('active');
            }
            $('#side-menu a[href="#'+tab+'"]').removeClass('active');
            $('#tab_' + tab).remove();
            $("#"+ tab).remove();
        },
        //功能id,功能的最顶层父级菜单url,功能url,功能名称
        createTab: function (tabId, dir, funcUrl,tabName) {
            if ($('#' + tabId).length == 0) {
                var title = $('<a href="#' + tabId + '" role="' + dir + '"data-parentrole="' + funcUrl + '" data-toggle="tab">' + tabName + '</a>');
                if ($('#navTabs li').length > 0) {
                    title.append('&nbsp;&nbsp;<i class="fa fa-times"></i>');
                }
                $('#navTabs').append($('<li id="tab_' + tabId + '"></li>').append(title));
                $('#tabContent').append('<div id="'+ tabId + '" role="tabpanel" class="tab-pane active"></div>');
                $('#' + tabId).append('<iframe scrolling="auto" frameborder="0" src="./' + funcUrl + '.html" style="width:100%;height:'+(document.documentElement.clientHeight-150)+'px;overflow:auto;"></iframe>');
            }
            $('#tab_' + tabId).addClass('active').siblings().removeClass('active');
            $('#' + tabId).addClass('active').siblings().removeClass('active');
            $('#navTabs a[data-toggle="tab"]').removeClass('active');
            $('#side-menu a[data-toggle="tab"]').removeClass('active');
            $('#side-menu a[href="#'+tabId+'"]').addClass('active');
        },
        createTabInSub: function (tabId, dir, tabName, params) {
            if ($('#' + tabId, window.parent.document).length > 0) {
                $('#' + tabId, window.parent.document).empty();
            } else {
                var title = $('<a href="#' + tabId + '" role="' + dir + '" data-toggle="tab">' + tabName + '</a>')
                    .append('<i class="close">&times;</i>');
                $('#navTabs', window.parent.document).append($('<li id="tab_' + tabId + '"></li>').append(title));
                $('#tabContent', window.parent.document).append('<div id="'+ tabId + '" role="tabpanel" class="tab-pane"></div>');
            }
            $('#' + tabId, window.parent.document).append(
                '<iframe scrolling="auto" frameborder="0" src="./' + dir + '/' + tabId + '.html' + (params ? '?' + params : '') + '" style="width:100%;height:'+document.documentElement.clientHeight+'px;overflow:hidden;"></iframe>'
            );
            $('#tab_' + tabId, window.parent.document).addClass('active').siblings().removeClass('active');
            $('#' + tabId, window.parent.document).addClass('active').siblings().removeClass('active');
        }
    }
});
