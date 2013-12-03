window.applicationContextPath = window.applicationContextPath || "";
var mocks = parent.parent ? parent.parent.mocks : parent.mocks;
mocks = mocks || [];
var dependencies = mocks.concat(["ngResource"]);
var app = angular.module("TodoApp", dependencies);

if (undefined != mocks.initializeMocks) {
    app.run(mocks.initializeMocks);
}

app.controller("TodoCtrl", function ($scope, NoteDAO)
{
    var EDIT_MODE = "edit";
    var mode;

    function refresh()
    {
        NoteDAO.query(function (data)
        {
            $scope.notes = data;
        });
    }

    $scope.isEditNoteMode = function ()
    {
        return EDIT_MODE === mode;
    };

    $scope.addNote = function ()
    {
        $scope.selectedNote = {};
        mode = EDIT_MODE;
    };

    $scope.cancel = function ()
    {
        mode = undefined;
    };

    $scope.save = function ()
    {
        NoteDAO.save($scope.selectedNote, refresh);
        mode = undefined;
    };

    $scope.remove = function (note)
    {
        NoteDAO.remove({id: note.id}, null, refresh);
    };

    refresh();
});

app.factory("NoteDAO", function ($resource)
{
    return $resource(window.applicationContextPath + "/rest/note/:id", {id: "@id"});
});
