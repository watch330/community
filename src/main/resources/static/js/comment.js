// function getComment(id) {
//     $.ajax({
//         url: "/getCommentList",
//         type: "post",
//         data: {"id": id},
//         dataType: "html",
//         success: function (data) {
//             $(".comment-list-container").html(data);
//         }
//     });
// }
function getComment(id) {
    $(".comment-list-container").load("/getCommentList #comment-list-container-mark", {"id": id})
}

function waitNav() {
    var stopID = setInterval(getNav, 1000);
    var stopValue = 0;


    function getNav() {
        if (window.localStorage.getItem("loginDone") == "true") {
            $("#nav-mark").load("/updateNavi #navibar-id");
            window.localStorage.removeItem("loginDone");
            clearInterval(stopID);
        }
        console.log("continue");
        stopValue += 1000;
        if (stopValue == 60000) {
            window.localStorage.removeItem("closeable");
            window.localStorage.removeItem("loginDone");
            clearInterval(stopID);
        }
    }


}

function comment(val, id, type, parentId) {
    if (!val) {
        alert("评论不能为空...");
        return;
    }

    $.ajax({
        url: "/comment",
        type: "post",
        data: JSON.stringify({"parentId": id, "content": val, "type": type}),
        success: function (data) {
            if (data.code == 200) {
                if (type == 1) {
                    getComment(id);
                    $("#comment-container").hide();

                } else {
                    getComment(parentId);
                    // $.post("/getCommentList",{"id":parentId},function (data) {
                    //     $(".comment-list-container").html($(data).find("#comment-list-container-mark").html())
                    // });
                    showCommentAfterPost(id);
                }
            } else {
                if (data.code == 101) {
                    var isAccepted = confirm(data.message + "请登录");
                    if (isAccepted) {
                        window.localStorage.setItem("closeable", true);
                        waitNav();
                        window.open("/login");

                    }
                } else
                    alert(data.message);
            }
        },
        dataType: "json",
        contentType: "application/json"
    });
}

function secondCommentPost(e) {
    const targetId = e.getAttribute("data-id");
    const parentId = e.getAttribute("data-parentId")
    const selector = "#second-comment-input-" + targetId;
    var content = $(selector).val();

    comment(content, targetId, 2,parentId);
}

function commentPost() {
    const id = $("#comment-id").val();
    const val = $("#comment").val();
    const type = $("#comment-type").val();

    comment(val, id, type,1);
}


function showComment(id) {
    $("#comment-container").show();
    $("#comment-id").val(id);
    $("#comment-type").val(1);
}

function show2ndComment(id) {
    $("#comment-container").show();
    $("#comment-id").val(id);
    $("#comment-type").val(2);
}

function deleteComment(parentId,id, type) {
    $.ajax({
        url: "/deleteComment",
        type: "post",
        data: {"id": id,"parentId":parentId, "type": type},
        dataType: "html",
        success: function (data) {
            if (type==1)
                getComment(parentId);
            else{
                $(".comment-list-container").html($(data).find("#comment-list-container-mark").html());
                showChildAfterDelete(parentId);
            }
        }
    });
}

function showChildAfterDelete(ID) {
    const target = "#comment-" + ID;
    $(target + " .second-mark").load("/comments .second-ajax-mark", {"id": ID});
    var commentListId = "#comment-" + ID;
    const btn = $(commentListId);

    $("#comment-btn-a"+ID).parent().toggleClass("comment-btn-active");
    btn.toggleClass("in");
}

function showCommentAfterPost(id) {
    const target = "#comment-" + id;
    $(target + " .second-mark").load("/comments .second-ajax-mark", {"id": id});
    $("#second-comment-input-"+id).val("");

    // $("#comment-btn-a"+id).parent().toggleClass("comment-btn-active");
    // $(target).toggleClass("in");
}

function showChild(e) {
    var ID = e.getAttribute("data-id");
    const target = "#comment-" + ID;
    $(target + " .second-mark").load("/comments .second-ajax-mark", {"id": ID});
    var commentListId = "#comment-" + ID;
    const btn = $(commentListId);

    $(e).parent().toggleClass("comment-btn-active");
    btn.toggleClass("in");
}