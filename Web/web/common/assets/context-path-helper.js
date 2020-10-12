function calculateContextPath() {
    const pathWithoutLeadingSlash = window.location.pathname.substring(1);
    const contextPathEndIndex = pathWithoutLeadingSlash.indexOf('/');
    return pathWithoutLeadingSlash.substr(0, contextPathEndIndex);
}

function wrapBuildingURLWithContextPath() {
    const contextPath = calculateContextPath();
    return function(resource) {
        return "/" + contextPath + "/" + resource;
    };
}

const buildUrlWithContextPath = wrapBuildingURLWithContextPath();