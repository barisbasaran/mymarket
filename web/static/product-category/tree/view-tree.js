$(document).ready(function () {
    viewProductCategoryTree();
});

function viewProductCategoryTree() {
    doGetRequest('/service/admin/product-categories/tree', (categoryNodes) => {
        let chart_config = new Map();
        chart_config["chart"] = {
            container: "#category-tree",
            connectors: {
                type: 'step'
            },
            node: {
                HTMLclass: 'nodeExample1'
            }
        };
        let top = {
            text: {
                name: "Top"
            },
            children: []
        }
        chart_config["nodeStructure"] = top;
        categoryNodes.forEach(categoryNode => {
            top.children.push(getChildren(categoryNode))
        })

        new Treant(chart_config);
    });
}

function getChildren(categoryNode) {
    let pc = {
        text: {
            name: categoryNode.name,
        },
        link: {
            href: "/product-category/view-details.html?pc=" + categoryNode.id
        },
        stackChildren: categoryNode.children.length > 0,
        children: []
    }
    categoryNode.children.forEach(cn => {
        pc.children.push(getChildren(cn))
    })
    return pc;
}
