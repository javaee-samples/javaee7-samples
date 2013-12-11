describe("TodoCtrl", function ()
{
    beforeEach(module("TodoApp"));
    describe("constructor", function ()
    {
        var $scope, noteDAOMock, expectedInitialNotes = [
            {title: "Hello"},
            {title: "Good bye"}
        ];
        beforeEach(inject(function ($rootScope, $controller)
        {
            $scope = $rootScope.$new();
            noteDAOMock = {query: jasmine.createSpy().andCallFake(function (success)
            {
                success(expectedInitialNotes);
            })};
            $controller("TodoCtrl", {$scope: $scope, NoteDAO: noteDAOMock});
        }));

        it("should send request to NoteDAO", function ()
        {
            expect(noteDAOMock.query).toHaveBeenCalled();
        });

        it("should have notes loaded", function ()
        {
            expect($scope.notes).toBe(expectedInitialNotes);
        });

        it("should NOT be in edit mode", function ()
        {
            expect($scope.isEditNoteMode()).toBe(false);
        });
    });

    describe("addNote", function ()
    {
        var $scope;
        beforeEach(inject(function ($rootScope, $controller)
        {
//            Given
            $scope = $rootScope.$new();
            $controller("TodoCtrl", {$scope: $scope});
//            When
            $scope.note = {title: "Hello"};
            $scope.addNote();
        }));

        it("should turn to edit mode", function ()
        {
            expect($scope.isEditNoteMode()).toBe(true);
        });

        it("should clear current note", function ()
        {
            expect($scope.selectedNote).toEqual({});
        });
    });

    describe("cancel", function ()
    {
        var $scope;
        beforeEach(inject(function ($rootScope, $controller)
        {
//            Given
            $scope = $rootScope.$new();
            $controller("TodoCtrl", {$scope: $scope});
//            When
            $scope.addNote();
            $scope.cancel();
        }));

        it("should not be in edit mode", function ()
        {
            expect($scope.isEditNoteMode()).toBe(false);
        });
    });

    describe("save", function ()
    {
        var $scope, noteDAOMock;
        beforeEach(inject(function ($rootScope, $controller)
        {
//            Given
            noteDAOMock = jasmine.createSpyObj("noteDAOMock", ["query", "save"]);
            noteDAOMock.save.andCallFake(function (note, success)
            {
                success();
            });
            $scope = $rootScope.$new();
            $controller("TodoCtrl", {$scope: $scope, NoteDAO: noteDAOMock});
//            When
            $scope.addNote();
            $scope.selectedNote.title = "New title";
            $scope.selectedNote.summary = "New summary";
            $scope.save();
        }));

        it("should not be in edit mode", function ()
        {
            expect($scope.isEditNoteMode()).toBe(false);
        });

        it("should invoke save on NoteDAO", function ()
        {
            expect(noteDAOMock.save).toHaveBeenCalledWith($scope.selectedNote, jasmine.any(Function));
        });

        it("should invoke refresh", function ()
        {
            expect(noteDAOMock.query.calls.length).toBe(2);
        });
    });

    describe("remove", function ()
    {
        var $scope, noteDAOMock, expectedInitialNotes = [
            {id: 1, title: "Hello"},
            {id: 2, title: "Good bye"}
        ];
        beforeEach(inject(function ($rootScope, $controller)
        {
            $scope = $rootScope.$new();
            noteDAOMock = jasmine.createSpyObj("noteDAOMock", ["query", "remove"]);
            noteDAOMock.remove.andCallFake(function (params, entity, success)
            {
                success(expectedInitialNotes);
            });
            $controller("TodoCtrl", {$scope: $scope, NoteDAO: noteDAOMock});
            expect(noteDAOMock.query.calls.length).toBe(1);
            $scope.remove(expectedInitialNotes[0]);
        }));

        it("should invoke NoteDAO.remove", function ()
        {
            expect(noteDAOMock.remove).toHaveBeenCalledWith({id: 1}, null, jasmine.any(Function));
        });

        it("should invoke refresh", function ()
        {
            expect(noteDAOMock.query.calls.length).toBe(2);
        });
    });
});
