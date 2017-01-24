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
var optionsAsString;
      for(var i = 0; i < pools.length; i++) {
        optionsAsString += "<option value='" + pools[i].name + "'>" + pools[i].name + "</option>";
      }
      $('#pool').append(optionsAsString);
      $("#jsGrid").jsGrid({
        height: "auto",
        width: "100%",

        filtering: true,
        editing: true,
        sorting: true,
        paging: true,
        autoload: true,

        deleteConfirm: "Do you really want to delete the resource?",

        rowClick: function(args) {
          showDetailsDialog("Edit", args.item);
        },

        pageSize: 15,
        pageButtonCount: 5,

        controller: {
            loadData: function(filter) {
                var d = $.Deferred();

                $.ajax({
                    url: "/api/resources",
                    dataType: "json"
                }).done(function(response) {
                    result = $.grep(response.content, function(resource) {
                      return (!filter.type || resource.type.indexOf(filter.type) > -1)
                              && (!filter.infos.ip || resource.infos.ip.indexOf(filter.infos.ip) > -1)
                              && (!filter.keywords || filter.keywords.split(',').every(function (one) {
                                return resource.keywords.includes(one);
                              }))
                              && (!filter.poolId || resource.poolId == filter.poolId);
                    });
                    d.resolve(result);
                });

                return d.promise();
            },
            deleteItem: function(item) {
                return $.ajax({
                    type: "DELETE",
                    url: "/api/resources/" + item.id,
                });
            },
            insertItem: function(item) {
                return $.ajax({
                    method: "POST",
                    url: "/api/resources",
                    contentType: "application/json",
                    data: JSON.stringify(item)
                })
            },

            updateItem: function(item) {
                return $.ajax({
                    method: "PUT",
                    url: "/api/resources/" + item.id,
                    contentType: "application/json",
                    data: JSON.stringify(item)
                })
            },
        },

        fields: [
            { title: "Resource Pool", name: "poolId", type: "text" },
            { title: "Type", name: "type", type: "text" },
            { title: "IP", name: "infos.ip", type: "text" },
            { title: "Keywords", name: "keywords", type: "text",
              itemTemplate:
                function(value) {
                    return value.join(", ")
                }
            },
            { title: "Owner", name: "owner", type: "text" },
            { title: "Status", name: "status", type: "text" },
            { title: "Is Booked", name: "bookingId", type: "checkbox" },
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

    var showDetailsDialog = function(dialogType, resource) {
      if(dialogType === "Add") {
        $.extend(resource, {
            type: "",
            keywords: [],
            poolId: "default"
        });
      }
      $("#type").val(resource.type);
      $("#keywords").val(resource.keywords);
      $("#pool").val(resource.poolId);
      $("#booked").prop("checked", resource.bookingId != null);
      $("#infos").val(JSON.stringify(resource.infos,null,2));

      formSubmitHandler = function() {
        saveResource(resource, dialogType === "Add");
      };
      $("#detailsDialog").dialog("option", "title", dialogType + " Resource")
        .dialog("open");
    };

    var saveResource = function(resource, isNew) {
        $.extend(resource, {
            type: $("#type").val(),
            keywords: $("#keywords").val().split(','),
            poolId: $("#pool").val(),
            infos: JSON.parse($("#infos").val())
        });

        $("#jsGrid").jsGrid(isNew ? "insertItem" : "updateItem", resource);
        $("#detailsDialog").dialog("close");
    };

  })