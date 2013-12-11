describe("Todo list", function ()
{
    beforeEach(function ()
    {
        mockApi().reset();
    });

    describe("on enter", function ()
    {
        beforeEach(function ()
        {
//            Given
            mockApi("get_notes").response({data: [
                {id: 1, title: "Hello"},
                {id: 2, title: "Good bye"}
            ]});

//            When
            browser().navigateTo("/");
        });

        it("should have 2 notes loaded", function ()
        {
            expect(repeater("#notes .note").count()).toBe(2);
        });

        it("should hide edit note modal", function ()
        {
            expect(element("#edit-note-form:visible").count()).toBe(0);
        });

        describe("and `remove` button click", function ()
        {
            beforeEach(function ()
            {
                mockApi("get_notes").response({data: [
                    {title: "Good bye"}
                ]});
                element("#notes .note:nth-of-type(1) .btn-danger").click();
            });

            it("should not contain element on list", function ()
            {
                expect(repeater("#notes .note").count()).toBe(1);
            });
        });
    });

    describe("on `add note` button click", function ()
    {
        var newNoteTitle = "New note title";
        var newNoteSummary = "New note summary";

        beforeEach(function ()
        {
//            Given
            mockApi("get_notes").response({data: [
                {title: "Hello"},
                {title: "Good bye"}
            ]});

            mockApi("post_note").response({data: {id: 3, title: newNoteTitle, summary: newNoteSummary}});

//            When
            browser().navigateTo("/");
            element("#add-note").click();
        });

        it("should a modal should be shown", function ()
        {
            expect(element("#edit-note-form:visible").count()).toBe(1);
        });

        it("should have all controls blank", function ()
        {
            expect(element("#edit-note-form input").val()).toBe("");
            expect(element("#edit-note-form textarea").text()).toBe("");
        });

        describe("and on `cancel` button click", function ()
        {
            beforeEach(function ()
            {
                element("#edit-note-form .btn-default").click();
            });

            it("should hide modal", function ()
            {
                expect(element("#edit-note-form:visible").count()).toBe(0);
            });
        });

        describe("and on filling form and `save` button click", function ()
        {
            beforeEach(function ()
            {
                mockApi("get_notes").response({data: [
                    {title: "1"},
                    {title: "2"},
                    {title: "new title"}
                ]});
                input("selectedNote.title").enter("new title");
                input("selectedNote.summary").enter("new summary");
                element("#edit-note-form .btn-primary").click();
            });

            it("should hide modal", function ()
            {
                expect(element("#edit-note-form:visible").count()).toBe(0);
            });

            it("should refresh list", function ()
            {
                expect(element("#notes .note").count()).toBe(3);
            });
        });
    });

});
