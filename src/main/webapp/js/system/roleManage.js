require(['../config'], function (config) {

    require(['jquery','popper', 'bootstrap', 'datatables', 'zTree','metisMenu','slimscroll','pace','inspinia'], function ($,Popper) {
        
    	Popper.Defaults.modifiers.computeStyle.gpuAcceleration = false;
        var basePath = config.basePath;

        var roleTable = $('#role-table').DataTable({
            ajax: {
                url: basePath + '/system/roleManage/getRoleList'
            },
            columns: [
                {data: null},
                {
                    data: {
                        _: 'role_name',
                        sort: 'role_name_sort'
                    }
                }
            ],
            columnDefs: [
                {
                    targets: 0,
                    orderable: false
                },
                {
                    targets: 2,
                    data: function () {
                        var operator = '<a class="modifyRole" href="#" title="修改角色"><span class="link modify"></span></a>'
                            + '&nbsp;&nbsp;&nbsp;' + '<a class="deleteRole" href="#" title="删除角色"><span class="link delete"></span></a>'
                            + '&nbsp;&nbsp;&nbsp;' + '<a class="authorize" href="#" title="分配权限"><span class="link auth"></span></a>';
                        return operator;
                    }
                }
            ],
            order: [[ 1, 'desc' ]],
            searching: false,
            lengthChange: false,
            info: false,
            paging:false
        });

        roleTable.on('order.dt search.dt', function() {
            roleTable.column(0, {
                search: 'applied',
                order: 'applied'
            }).nodes().each(function(cell, i) {
                cell.innerHTML = i + 1;
            });
        }).draw();

        $('#btnRefresh').click(function() {
            roleTable.ajax.reload();
        });

        function createRole (e) {
            console.log(e.data.type);
            if (e.data.type == 'keypress' && e.keyCode == 27) {
                roleTable.row($(e.target).parents('tr')).remove().draw(false);
            } else if ((e.data.type == 'keypress' && e.keyCode == 13)) {
                $(e.target).blur();
            } else if (e.data.type == 'blur') {
                var roleName = $.trim($(e.target).val());
                if (roleName == '') {
                    // alert('角色名称不能为空');
                    roleTable.row($(e.target).parents('tr')).remove().draw(false);
                    return;
                }
                $.ajax({
                    url: basePath + '/system/roleManage/createRole',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        role_name: roleName
                    }),
                    dataType: 'json',
                    success: function (result) {
                        if (result != null && result.code == 0) {
                            roleTable.row($(e.target).parents('tr')).data(result.data).draw();
                        } else {
                            alert(result.msg || '新建角色异常请刷新后再次尝试！');
                            roleTable.row($(e.target).parents('tr')).remove().draw( false );
                        }
                    }
                });
            }
        }

        $('#btnRoleCreate').on('click', function () {
            if ($('#role-table .form-control').length > 0) return;
            $('#btnBuild').click();
            roleTable.row.add({
                role_id: null,
                role_name: '<input type="text" class="form-control create-role" placeholder="角色名称" />',
                role_name_sort: '',
                role_code: null
            }).draw(false);

            $('#role-table .create-role').focus();
            $('#role-table .create-role').bind('blur', { type: 'blur' }, createRole)
                .bind('keypress', { type: 'keypress' }, createRole);
        });

        function modifyRole (e) {
            var tr = $(e.target).parents('tr');
            var data = roleTable.row(tr).data();
            if (e.data.type == 'keypress' && e.keyCode == 27) {
                data.role_name = e.data.roleName;
                roleTable.row(tr).data(data).draw(false);
            } else if ((e.data.type == 'keypress' && e.keyCode == 13)) {
                $(e.target).blur();
            } else if (e.data.type == 'blur') {
                var roleName = $.trim($(e.target).val());
                if (roleName == '') {
                    // alert('角色名称不能为空');
                    data.role_name = e.data.roleName;
                    roleTable.row(tr).data(data).draw(false);
                    return;
                }
                $.ajax({
                    url: basePath + '/system/roleManage/modifyRole',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        role_id: data.role_id,
                        role_name: roleName
                    }),
                    dataType: 'json',
                    success: function (result) {
                        if (result != null && result.code == 0) {
                            data.role_name = roleName;
                            roleTable.row(tr).data(data).draw();
                        } else {
                            alert(result.msg || '修改角色异常请刷新后再次尝试！');
                            data.role_name = e.data.roleName;
                            roleTable.row(tr).data(data).draw(false);
                        }
                    }
                });
            }
        }

        $('#role-table').on('click', '.modifyRole', function (e) {
            if ($('#role-table .form-control').length > 0) return;
            $('#btnBuild').click();
            var data = roleTable.row($(e.target).parents('tr')).data();
            var roleName = data.role_name;
            data.role_name = '<input type="text" class="form-control modify-role" value="' + roleName + '" placeholder="角色名称" />';
            roleTable.row($(e.target).parents('tr')).data(data).draw(false);

            $('#role-table .modify-role').focus().select();
            $('#role-table .modify-role').bind('blur', { type: 'blur', roleName: roleName }, modifyRole)
                .bind('keypress', { type: 'keypress', roleName: roleName }, modifyRole);
        });

        $('#role-table').on('click', '.deleteRole', function (e) {
            if ($('#role-table .form-control').length > 0) return;
            $('#btnBuild').click();
            var data = roleTable.row($(e.target).parents('tr')).data();
            if (data.role_id == 1) {
                alert('不能删除管理员账户！');
                return;
            }
            if (confirm('确认删除"' + data.role_name + '"的角色吗？\n将同时删除为该角色授予的权限，请慎重！')) {
                $.ajax({
                    url: basePath + "/system/roleManage/deleteRole",
                    data: $.param({ roleId: data.role_id }),
                    dataType: 'json',
                    success: function (result) {
                        if (result != null && result.code == 0) {
                            roleTable.row($(e.target).parents('tr')).remove().draw();
                        } else {
                            alert(result.msg || '删除角色异常请刷新后再次尝试！')
                        }
                    }
                })
            }
        });

        var setting = {
            async: {
                enable: true,
                url: basePath + '/system/roleManage/getFuncList',
                type: 'get',
                dataFilter: function (treeId, parentNode, childNodes) {
                    if (childNodes) {
                        for (var i = 0; i < childNodes.length; i++) {
                            if (!childNodes[i]['parent_id']) {
                                childNodes[i]['parent_id'] = 0;
                            }
                        }
                    } else {
                        childNodes = [];
                    }
                    childNodes.push({ id: 0, func_name: '根Root', parent_id: null, nocheck: true });
                    return childNodes;
                }
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: 'id',
                    pIdKey: 'parent_id',
                    rootPId: 0
                },
                key: {
                    name: 'func_name'
                }
            },
            view: {
                selectedMulti: false,
                addHoverDom: function (treeId, treeNode) {
                    $('#' + treeNode.tId + '_edit').unbind().bind('click', function () {
                        $(this).attr('data-tid', treeNode.tId);
                        $(this).attr('data-toggle', 'modal');
                        $(this).attr('data-target', '#funcModal');
                    });
                    var zTree = $.fn.zTree.getZTreeObj(treeId);
                    if (treeNode.editNameFlag || $('#addBtn_' + treeNode.tId).length > 0 || zTree.setting.check.enable || treeNode.level > 2) return;
                    var sObj = $('#' + treeNode.tId + '_span');
                    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                        + "' title='创建节点' onfocus='this.blur();' data-tid='" + treeNode.tId + "' data-toggle='modal' data-target='#funcModal'></span>";
                    sObj.after(addStr);
                },
                removeHoverDom: function (treeId, treeNode) {
                    $('#addBtn_' + treeNode.tId).unbind().remove();
                }
            },
            edit: {
                enable: true,
                renameTitle: '编辑节点',
                removeTitle: '删除节点',
                showRenameBtn: function (treeId, treeNode) {
                    return treeNode.level > 0 ? true : false;
                },
                showRemoveBtn: function (treeId, treeNode) {
                    return treeNode.level > 0 ? true : false;
                }
            },
            callback: {
                beforeRemove: function (treeId, treeNode) {
                    var zTree = $.fn.zTree.getZTreeObj(treeId);
                    zTree.selectNode(treeNode);
                    if (confirm('确认删除"' + treeNode['func_name'] + '"的功能节点吗？\n将同时删除该节点的所有子节点及相关授权，请慎重！')) {
                        var flag = true;
                        $.ajax({
                            async: false,
                            url: basePath + '/system/roleManage/deleteFunc',
                            data: $.param({ id: treeNode.id }),
                            dataType: 'json',
                            success: function (result) {
                                if (result.code != 0) {
                                    flag = false;
                                    alert(result.msg);
                                } else {
                                    console.log('remove node success');
                                }
                            }
                        });
                        return flag;
                    }
                    return false;
                },
                onAsyncSuccess: function (event, treeId, treeNode, msg) {
                    var zTree = $.fn.zTree.getZTreeObj(treeId);
                    zTree.expandAll(true);
                }
            }
        };

        $.fn.zTree.init($("#funcTree"), setting, null);

        $('#funcModal').on('show.bs.modal', function (e) {
            var tId = $(e.relatedTarget).data('tid');
            if ($(e.relatedTarget).hasClass('add')) {
                addFunction(tId);
            } else {
                modifyFunction(tId);
            }
        });

        function addFunction (tId) {
            $('#funcModalLabel').text('新增功能');
            $('#funcName, #funcCode, #funcOrder, #funcDesc').val('');
            var zTree = $.fn.zTree.getZTreeObj("funcTree");
            var treeNode = zTree.getNodeByTId(tId);
            $('#funcParent').val(treeNode['func_name']);
            $('#btnFunc').unbind().bind('click', function (e) {
                e.preventDefault();
                if (!$('#funcName').val() || !$('#funcCode').val()) {
                    alert('信息不完整');
                    return;
                }
                $.ajax({
                    url: basePath + '/system/roleManage/createFunc',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        func_name: $('#funcName').val(),
                        func_url: $('#funcCode').val(),
                        parent_id: treeNode['id'] || null,
                        func_level: treeNode.level + 1,
                        show_order: $('#funcOrder').val(),
                        func_desc: $('#funcDesc').val()
                    }),
                    success: function (result) {
                        if (result != null) {
                            $('#funcModal').modal('hide');
                            zTree.addNodes(treeNode, result);
                        } else {
                            alert('创建失败，请查看网络连接');
                        }
                    }
                })
            });
        }

        function modifyFunction (tId) {
            $('#funcModalLabel').text('修改功能');
            var zTree = $.fn.zTree.getZTreeObj("funcTree");
            var treeNode = zTree.getNodeByTId(tId);
            $('#funcName').val(treeNode['func_name']);
            $('#funcCode').val(treeNode['func_url']);
            $('#funcOrder').val(treeNode['show_order']);
            $('#funcDesc').val(treeNode['func_desc']);
            $('#funcParent').val(treeNode.getParentNode()['func_name']);
            $('#funcParentId').val(treeNode.getParentNode()['id']);
            //console.log(treeNode.getParentNode());
            $('#btnFunc').unbind().bind('click', function (e) {
                e.preventDefault();
                if (!$('#funcName').val() || !$('#funcCode').val()) {
                    alert('信息不完整');
                    return;
                }
                var params = {
                    id: treeNode['id'],
                    func_name: $('#funcName').val(),
                    parent_id: $('#funcParentId').val(),
                    func_url: $('#funcCode').val(),
                    show_order: $('#funcOrder').val(),
                    func_desc: $('#funcDesc').val()
                };
                $.ajax({
                    url: basePath + '/system/roleManage/modifyFunc',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(params),
                    success: function (result) {
                        if (result.code == 0) {
                            $('#funcModal').modal('hide');
                            $.extend(treeNode, params);
                            zTree.updateNode(treeNode);
                        } else {
                            alert('创建失败，请查看网络连接');
                        }
                    }
                })
            });
        }

        $('#role-table tbody').on('click', '.authorize', function () {
            if ($('#role-table .form-control').length > 0) return;
            var zTree = $.fn.zTree.getZTreeObj("funcTree");
            if (!zTree) return;
            if (!zTree.setting.check.enable) {
                zTree.setting.check.enable = true;
                zTree.setEditable(false);
                zTree.setting.check.chkboxType = { "Y": "p", "N": "s" };
                $('#btnTreeSave').show();
                $('#btnBuild').removeAttr('disabled');
            }
            zTree.checkAllNodes(false);
            var nodes = zTree.transformToArray(zTree.getNodes());
            $.each(nodes, function (idx, node) {
                if (!node.nocheck) {
                    node.checkedOld = false;
                    // zTree.updateNode(node);
                }
            });
            var tr = $(this).parents('tr');
            var row = roleTable.row(tr).data();
            $('#funcTitle').text('为' + row.role_name + '授权');
            $('#btnTreeSave').attr('data-role', row.role_id);
            $.ajax({
                url: basePath + '/system/roleManage/getPermission',
                data: $.param({ roleId: row.role_id }),
                dataType: 'json',
                success: function (result) {
                    if (result != null && result.funcIds != null) {
                        $.each(result.funcIds, function (idx, obj) {
                            var node = zTree.getNodeByParam('id', obj, null);
                            if (node != null) {
                                zTree.checkNode(node, true, true);
                                node.checkedOld = true;
                            }
                        });
                    }
                }
            });
        });

        $('#btnBuild').on('click', function () {
            var zTree = $.fn.zTree.getZTreeObj("funcTree");
            if (!zTree) return;
            zTree.setting.check.enable = false;
            zTree.setEditable(true);
            // zTree.refresh();
            this.disabled = 'disabled';
            $('#funcTitle').text('功能列表');
            $('#btnTreeSave').hide().removeAttr('data-role');
        });

        $('#btnTreeSave').on('click', function () {
            var roleId = $(this).attr('data-role');
            if (roleId) {
                var zTree = $.fn.zTree.getZTreeObj("funcTree");
                var changedNodes = zTree.getChangeCheckedNodes();
                if (changedNodes.length > 0) {
                    var addArr = [], cancelArr = [];
                    $.each(changedNodes, function (idx, obj) {
                        console.log(obj);
                        if (obj.checkedOld) {
                            cancelArr.push(obj.id);
                        } else {
                            addArr.push(obj.id);
                        }
                    });
                    $.ajax({
                        url: basePath + '/system/roleManage/updatePermission',
                        type: 'POST',
                        data: JSON.stringify({
                            roleId: roleId,
                            add: addArr.join(','),
                            cancel: cancelArr.join(',')
                        }),
                        contentType: 'application/json',
                        dataType: 'json',
                        success: function (result) {
                            if (result != null && result.code == 0) {
                                $.each(changedNodes, function (idx, obj) {
                                    obj.checkedOld = obj.checked;
                                    // zTree.updateNode(obj);
                                });
                                alert('授权成功');
                            } else {
                                alert(result.msg || '服务器响应异常，请刷新后再次尝试！');
                            }
                        }
                    });
                }
            }
        });

        $('#btnTreeRefresh').on('click', function () {
            var zTree = $.fn.zTree.getZTreeObj("funcTree");
            if (zTree.setting.check.enable) {
                zTree.setting.check.enable = false;
                zTree.setEditable(true);
                $('#btnBuild').attr('disabled', 'disabled');
                $('#funcTitle').text('功能列表');
                $('#btnTreeSave').hide().removeAttr('data-role');
            }
            zTree.reAsyncChildNodes(null, "refresh");
        });

    });
});