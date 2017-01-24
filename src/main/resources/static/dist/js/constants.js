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
        var optionsAsString;
              for(var i = 0; i < pools.length; i++) {
                optionsAsString += "<option value='" + pools[i].name + "'>" + pools[i].name + "</option>";
              }
              $('#pool').append(optionsAsString);
        pools = [{"id": "","name": ""}].concat(pools)

        $("#jsGrid").jsGrid({
            height: "auto",
            width: "100%",

            filtering: true,
            editing: true,
            sorting: true,
            paging: true,
            autoload: true,

            deleteConfirm: "Do you really want to delete the constant data?",

            rowClick: function(args) {
                showDetailsDialog("Edit", args.item);
            },

            pageSize: 10,
            pageButtonCount: 5,

            controller: {
                loadData: function(filter) {
                    var d = $.Deferred();

                    $.ajax({
                        url: "/api/constants",
                        dataType: "json"
                    }).done(function(response) {
                        result = $.grep(response, function(constants) {
                            return (!filter.poolName || constants.poolName.indexOf(filter.poolName) > -1)
                                && (!filter.constants || constants.constants.indexOf(filter.constants) > -1);
                        });
                        d.resolve(result);
                    });

                    return d.promise();
                },
                deleteItem: function(item) {
                    return $.ajax({
                        type: "DELETE",
                        url: "/api/constants/" + item.id,
                    });
                },
                insertItem: function(item) {
                    return $.ajax({
                        method: "POST",
                        url: "/api/constants",
                        contentType: "application/json",
                        data: JSON.stringify(item)
                    })
                },

                updateItem: function(item) {
                    return $.ajax({
                        method: "PUT",
                        url: "/api/constants/" + item.id,
                        contentType: "application/json",
                        data: JSON.stringify(item)
                    })
                },
            },

            fields: [
                { title: "Resource Pool", name: "poolName", type: "text" },
                {title: "Constant Data", name: "constants", type: "text",
                    itemTemplate:
                        function(value) {
                            if(value != null) {
                                return JSON.stringify(value,null,2)
                            }
                        }
                },
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

    var showDetailsDialog = function(dialogType, constants) {
        if(dialogType === "Add") {
            $.extend(constants, {
                poolName: "default",
                constants: {}
            });
        }
        $("#pool").val(constants.poolName);
        $("#constants").val(JSON.stringify(constants.constants,null,2));

        formSubmitHandler = function() {
            saveResource(constants, dialogType === "Add");
        };
        $("#detailsDialog").dialog("option", "title", dialogType + " Constant Data")
            .dialog("open");
    };

    var saveResource = function(constants, isNew) {
        $.extend(constants, {
            poolName: $("#pool").val(),
            constants: JSON.parse($("#constants").val())
        });

        $("#jsGrid").jsGrid(isNew ? "insertItem" : "updateItem", constants);
        $("#detailsDialog").dialog("close");
    };

})