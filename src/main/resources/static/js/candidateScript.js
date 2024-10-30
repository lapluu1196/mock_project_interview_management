// $(document).ready(function () {
//     $("#filterByStatus").on("change", function () {
//         const search = $('#searchCandidate').val();
//         const status = $('#filterByStatus').val();
//
//         $.ajax({
//             type: 'GET',
//             url: '/candidates/filter',
//             data: {
//                 search: search,
//                 role: status
//             },
//             success: function (response) {
//                 $('#candidateTable').html(response);
//             },
//             error: function () {
//                 alert("Error fetching data");
//             }
//         });
//     });
// })