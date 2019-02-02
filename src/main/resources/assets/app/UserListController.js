function TodoListController() {
    "use strict";

    var self = this;
    var _hub;
    var _model;
    var _url = "/user/";

    self.name = "UserListControllerDefault";

    self.getComponentName = function() {
        return self.name;
    };

    self.userListRender = null; //RactiveRenderService - UserListRender

    /**
     * Configure the instance of the UserListController.
     *
     * @method configure
     * @param {HUBU.hub} theHub
     * @param conf - The UserListController configuration.
     * @param {map} conf.model - The model link to this UserListController
     * @param {string} [conf.url='/user'] - The root URL of the users
     */
    self.configure = function(theHub, conf) {
        _hub = theHub;

        if (typeof conf == "undefined") {
            throw new Exception("The UserListController configuration is mandatory.");
        }

        if (typeof conf.model !== "object") {
            throw new Exception("The model entry is mandatory.");
        }

        //Check with a regexp
        if (typeof conf.url === "string") {
            _url = conf.url;
        }

        _model = conf.model;

        _hub.requireService({
            component: this,
            contract: window.RactiveRenderService,
            field: "userListRender"
        });
    };

    function encodeIdURL(root,id) {
      return (root+id).replace("#","%23").replace(":","%3A");
    }

    function newTodo(event) {

        var newtodo = {
            content: event.node.value,
            done: "false"
        };

        $.ajax({
            type: "PUT",
            contentType: "application/json; charset=UTF-8",
            url: _model.url,
            data: JSON.stringify(newtodo)
        }).done(function(data) {
            _model.todos.push(data);
        });
    }

    function updateUser(event) {
        var user = event.context;

        $.ajax({
            type: "POST",
            contentType: "application/json; charset=UTF-8",
            url: encodeIdURL(_url+"/",user.id),
            data: JSON.stringify(todo)
        }).done(function() {
            //TODO notification
        });
    }

    function deleteUser(event, index) {
        var user = event.context;

        $.ajax({
            type: "DELETE",
            url: encodeIdURL(_url+"/",user.uuid)
        }).done(function() {
            _model.users.splice(index, 1);
        });
    }

    self.start = function() {

        $.ajax(_url).then(function(userList) {
            _model.users = userList;

            //render the todolist
            self.userListRender.render();

            self.userListRender.on("addUser", addUser);

            self.userListRender.on("updateUser", updateUser);

            self.userListRender.on("deleteUser", deleteUser);
        });
    };

    self.stop = function() {

    };
}
