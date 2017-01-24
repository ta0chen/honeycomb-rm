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
        url: "/api/pools",
        dataType: "json",
    }).done(function(response) {
        pools = response;
        pools = [{"id": "","name": ""}].concat(pools)

        $("#jsGrid").jsGrid({
            height: "auto",
            width: "100%",

            filtering: true,
            editing: true,
            sorting: true,
            paging: true,
            autoload: true,

            deleteConfirm: "Do you really want to delete the resource pool?",

            rowClick: function(args) {
                showDetailsDialog("Edit", args.item);
            },

            pageSize: 10,
            pageButtonCount: 5,

            controller: {
                loadData: function(filter) {
                    var d = $.Deferred();

                    $.ajax({
                        url: "/api/pools",
                        dataType: "json"
                    }).done(function(response) {
                        result = $.grep(response, function(resourcePool) {
                            return (!filter.name || resourcePool.name.indexOf(filter.name) > -1)
                                && (!filter.description || resourcePool.description.indexOf(filter.description) > -1)
                                && (!filter.owner || resourcePool.owner.indexOf(filter.owner) > -1);
                        });
                        d.resolve(result);
                    });

                    return d.promise();
                },
                deleteItem: function(item) {
                    return $.ajax({
                        type: "DELETE",
                        url: "/api/pools/" + item.id,
                    });
                },
                insertItem: function(item) {
                    return $.ajax({
                        method: "POST",
                        url: "/api/pools",
                        contentType: "application/json",
                        data: JSON.stringify(item)
                    })
                },

                updateItem: function(item) {
                    return $.ajax({
                        method: "PUT",
                        url: "/api/pools/" + item.id,
                        contentType: "application/json",
                        data: JSON.stringify(item)
                    })
                },
            },

            fields: [
                { title: "Name", name: "name", type: "text" },
                { title: "Description", name: "description", type: "text" },
                {title: "Owner", name: "owner", type: "text"},
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

    var showDetailsDialog = function(dialogType, resourcePool) {
        if(dialogType === "Add") {
            $.extend(resourcePool, {
                name: "",
                owner: "",
                description: ""
            });
        }
        $("#poolName").val(resourcePool.name);
        $("#description").val(resourcePool.description);
        $("#owner").val(resourcePool.owner);

        formSubmitHandler = function() {
            saveResource(resourcePool, dialogType === "Add");
        };
        $("#detailsDialog").dialog("option", "title", dialogType + " Resource Pool")
            .dialog("open");
    };

    var saveResource = function(resourcePool, isNew) {
        $.extend(resourcePool, {
            name: $("#poolName").val(),
            description: $("#description").val(),
            owner: $("#owner").val()
        });

        $("#jsGrid").jsGrid(isNew ? "insertItem" : "updateItem", resourcePool);
        $("#detailsDialog").dialog("close");
    };

})