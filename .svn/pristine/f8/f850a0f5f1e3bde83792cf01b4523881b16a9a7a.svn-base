$(function() {
    var pools = [];

    $("#detailsDialog").dialog({
        autoOpen: false,
        width: 400,
        close: function() {
            $("#detailsForm").find(".error").removeClass("error");
        }
    });

    $("#detailsForm").validate({
        submitHandler: function() {
            formSubmitHandler();
        }
    });

    var formSubmitHandler = $.noop;

    $.ajax({
        url: "/api/roles",
        dataType: "json",
    }).done(function (roleResponse) {
        roles = roleResponse;

        var optionsAsString;
        for(var i = 0; i < roles.length; i++) {
            optionsAsString += "<option value='" + roles[i].name + "'>" + roles[i].name + "</option>";
        }
        $('#roleList').append(optionsAsString);

        roles = [{"id": "","name": ""}].concat(roles)

        $.ajax({
            url: "/api/pools",
            dataType: "json",
        }).done(function(response) {
            pools = response;

            var optionsAsString;
            for(var i = 0; i < pools.length; i++) {
                optionsAsString += "<option value='" + pools[i].name + "'>" + pools[i].name + "</option>";
            }
            $('#poolList').append(optionsAsString);

            pools = [{"id": "","name": ""}].concat(pools)

            $("#jsGrid").jsGrid({
                height: "auto",
                width: "100%",

                filtering: true,
                editing: true,
                sorting: true,
                paging: true,
                autoload: true,

                deleteConfirm: "Do you really want to delete the user?",

                rowClick: function(args) {
                    showDetailsDialog("Edit", args.item);
                },

                pageSize: 15,
                pageButtonCount: 5,

                controller: {
                    loadData: function(filter) {
                        var d = $.Deferred();

                        $.ajax({
                            url: "/api/users",
                            dataType: "json"
                        }).done(function(response) {
                            result = $.grep(response, function(user) {
                                return (!filter.userName || user.userName.indexOf(filter.userName) > -1)
                                    && (!filter.firstName || user.firstName.indexOf(filter.firstName) > -1)
                                    && (!filter.lastName || user.lastName.indexOf(filter.lastName) > -1)
                                    && (!filter.email || user.email.indexOf(filter.email) > -1)
                                    && (!filter.poolList || user.poolList.indexOf(filter.poolList) > -1)
                                    && (!filter.roleList || user.roleList.indexOf(filter.roleList) > -1);
                            });
                            d.resolve(result);
                        });

                        return d.promise();
                    },
                    deleteItem: function(item) {
                        return $.ajax({
                            type: "DELETE",
                            url: "/api/users/" + item.id,
                        });
                    },
                    insertItem: function(item) {
                        return $.ajax({
                            method: "POST",
                            url: "/api/users",
                            contentType: "application/json",
                            data: JSON.stringify(item)
                        })
                    },

                    updateItem: function(item) {
                        return $.ajax({
                            method: "PUT",
                            url: "/api/users/" + item.id,
                            contentType: "application/json",
                            data: JSON.stringify(item)
                        })
                    },
                },

                fields: [
                    { title: "User Name", name: "userName", type: "text" },
                    { title: "First Name", name: "firstName", type: "text" },
                    {title: "Last Name", name: "lastName", type: "text"},
                    {title: "Email Address", name: "email", type: "text"},
                    {title: "Pool List", name: "poolList", type: "text",
                        itemTemplate:
                            function(value) {
                                if(value != null) {
                                    return value.join(", ")
                                }
                            }
                    },
                    {title: "Role List", name: "roleList", type: "text"},
                    {
                        type: "control",
                        modeSwitchButton: false,
                        editButton: false,
                        headerTemplate: function() {
                            return $("<button>").attr("type", "button").text("Add")
                                .on("click", function () {
                                    showDetailsDialog("Add", {});
                                });
                        }
                    }
                ]
            });
        });

    });

    var showDetailsDialog = function(dialogType, user) {
        if(dialogType === "Add") {
            $.extend(user, {
                userName: "",
                firstName: "",
                lastName: "",
                email: "",
                password: "",
                poolList: [],
                roleList: []
            });
        }
        $("#userName").val(user.userName);
        $("#firstName").val(user.firstName);
        $("#lastName").val(user.lastName);
        $("#email").val(user.email);
        $("#password").val(user.password);
        $("#poolList").val(user.poolList) || [];
        $("#roleList").val(user.roleList) || [];

        if(dialogType === "Edit") {
            document.getElementById("poolList").disabled = true;
            document.getElementById("roleList").disabled = true;
        } else {
            document.getElementById("poolList").disabled = false;
            document.getElementById("roleList").disabled = false;
        }

        formSubmitHandler = function() {
            saveResource(user, dialogType === "Add");
        };
        $("#detailsDialog").dialog("option", "title", dialogType + " User")
            .dialog("open");
    };

    var saveResource = function(user, isNew) {
        $.extend(user, {
            userName: $("#userName").val(),
            firstName: $("#firstName").val(),
            lastName: $("#lastName").val(),
            email: $("#email").val(),
            password: $("#password").val(),
            poolList: $("#poolList").val() || [],
            roleList: $("#roleList").val() || []
        });

        $("#jsGrid").jsGrid(isNew ? "insertItem" : "updateItem", user);
        $("#detailsDialog").dialog("close");
    };

})