function category_hireachy(categoryList) {
    // JSON 데이터 반복
    let html = "";
    categoryList.forEach(function(category) {

        // ctgHierachy가 1인 경우
        if (category.ctgHierachy === 1) {
            html += '<hr class="sidebar-divider">';
            html += '<div class="sidebar-heading">' + category.ctgTitle + '</div>';
        }

        // ctgHierachy가 2인 경우
        if (category.ctgHierachy === 2) {
            var childCategories = categoryList.filter(function(childCategory) {
                return childCategory.ctgPrtId === category.ctgId;
            });

            if (childCategories.length > 0) {
                html += '<li class="nav-item">';
                html += '<a class="nav-link collapsed" href="/' + category.ctgId + '" data-toggle="collapse" data-target="#collapsePages' + category.ctgId + '"';
                html += ' aria-expanded="true" aria-controls="collapsePages' + category.ctgId + '">';
                html += '<i class="fas fa-fw fa-folder"></i>';
                html += '<span>' + category.ctgTitle + '</span>';
                html += '</a>';

                html += '<div id="collapsePages' + category.ctgId + '" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">';
                html += '<div class="bg-white py-2 collapse-inner rounded">';

                childCategories.forEach(function(childCategory) {
                    html += '<a class="collapse-item" href="/' + childCategory.ctgId + '">' + childCategory.ctgTitle + '</a>';
                });

                html += '</div></div></li>';
            } else {
                html += '<li class="nav-item">';
                html += '<a class="nav-link" href="/' + category.ctgId + '">';
                html += '<i class="fas fa-fw fa-table"></i>';
                html += '<span>' + category.ctgTitle + '</span></a></li>';
            }
        }

    });

    return html;
}