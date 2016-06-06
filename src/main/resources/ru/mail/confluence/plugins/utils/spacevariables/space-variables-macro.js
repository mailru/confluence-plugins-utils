(function($) {
    AJS.bind("init.rte", function () {
        var propertyPanelMacroHandle = AJS.Confluence.PropertyPanel.Macro.handle;
        AJS.Confluence.PropertyPanel.Macro.handle = function(data) {
            if (data.containerEl.attributes.getNamedItem('data-macro-name').value == 'space-variables-macro')
                return;
            propertyPanelMacroHandle(data);
        };
    });
})(AJS.$);