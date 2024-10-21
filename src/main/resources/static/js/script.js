$(document).ready(function () {
    const $expandBtn = $(".toggle-btn");

    $expandBtn.on("click", function () {
        $("#sidebar").toggleClass("expand");
        $("#sidebar .toggle-btn img").toggleClass("rotate");
    });

    const $hamburgerDiv = $("#hamburgerButton");
    const $hamburgerBtn = $hamburgerDiv.find("button");
    const $sidebar = $("#sidebar");
    const $rightBoard = $("#rightBoard").children().not("#hamburgerButton");

    $hamburgerBtn.on("click", function (event) {
        event.stopPropagation();

        $sidebar.toggleClass("active");

        if ($sidebar.hasClass("active")) {
            $hamburgerDiv.addClass("non-active");
        } else {
            $hamburgerDiv.removeClass("non-active");
        }
    });

    $rightBoard.on("click", function () {
        if ($sidebar.hasClass("active")) {
            $sidebar.removeClass("active");
            $hamburgerDiv.removeClass("non-active");
        }
    });

    const title = $('title').text();
    $('#pageTitle').text(title);
  
    /* Back button */
    $('#cancelButton').on('click', function () {
        window.history.back();
    });

    // Filter by Role

    $("#filterByRole").on("change", function () {
        const search = $('#searchUser').val();
        const role = $('#filterByRole').val();

        $.ajax({
            type: 'GET',
            url: '/users/filter',
            data: {
                search: search,
                role: role
            },
            success: function (response) {
                $('#userTable').html(response);
            },
            error: function () {
                alert("Error fetching data");
            }
        });
    });

    /* Active and Inactive button */

    $("#activeUserButton").on("click", function () {
        const id = $(this).data('user-id');

        $.ajax({
            type: 'POST',
            url: '/api/users/updateStatus',
            data: {
                id: id
            },
            success: function (response) {
                if (response === 'Active') {
                    $('#activeUserButton').removeClass('btn-success').addClass('btn-danger');
                    $('#activeUserButton').text('Deactivate user');
                } else {
                    $('#activeUserButton').removeClass('btn-danger').addClass('btn-success');
                    $('#activeUserButton').text('Activate user');
                }
                $("#userStatus").html(response);
            },
            error: function () {
                alert("Error updating user status!");
            }
        });
    });

    /* Logout button */
    $("#logoutOkBtn").on("click", function () {
        window.location.href = '/logout';
    })
});