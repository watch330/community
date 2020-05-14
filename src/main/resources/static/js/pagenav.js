function nav(index) {
    // var path="?pageNum="+index;
    $.ajax({
        url: "/getPageInfo",
        type: "post",
        data: {"pageNum": index},
        dataType: "html",
        success: function (data) {

            console.log(data)
            $("body").html(data);
        }
    });
}