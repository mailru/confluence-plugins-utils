require(['jquery', 'backbone', 'utils/space-variables-configuration-dialog'], function($, Backbone, ConfigurationDialog) {
    AJS.toInit(function() {
        /* Models */
        var SpaceVariable = Backbone.Model.extend();
        var SpaceVariableView = Backbone.Model.extend({urlRoot: AJS.contextPath() + '/rest/confluence-utils/1.0/spacevariable/'});

        /* Collections */
        var SpaceVariableCollection = Backbone.Collection.extend({
            model: SpaceVariable,
            url: AJS.contextPath() + '/rest/confluence-utils/1.0/spacevariable?spaceKey=' + AJS.params.spaceKey
        });

        /* Instances */
        var spaceVariableCollection = new SpaceVariableCollection();

        /* View */
        var MainView = Backbone.View.extend({
            el: 'td.in-page-menu-content, div#space-tools-body',
            events: {
                'click #add-space-variable': 'addSpaceVariable',
                'click .space-variables-edit': 'editSpaceVariable',
                'click .space-variables-delete': 'deleteSpaceVariable'
            },
            initialize: function() {
                this.collection.on('remove', this._rebuildSpaceVariableList);
                this.collection.on('change', this._rebuildSpaceVariableList);
                this.collection.on('add', this._rebuildSpaceVariableList);
            },
            startLoadingSpaceVariables: function() {
                AJS.dim();
                Confluence.PageLoadingIndicator().show();
            },
            finishLoadingSpaceVariables: function() {
                Confluence.PageLoadingIndicator().hide();
                AJS.undim();
            },
            addSpaceVariable : function(e) {
                e.preventDefault();

                var configurationDialogView = new ConfigurationDialog({
                    model: new SpaceVariableView(),
                    collection: mainView.collection
                });
                configurationDialogView.show();
            },
            editSpaceVariable: function(e){
                e.preventDefault();

                var spaceVariableId = $(e.currentTarget).parents('tr').data('id');
                var spaceVariableView  = new SpaceVariableView({id: spaceVariableId});
                spaceVariableView.fetch({
                    success: $.proxy(function(model) {
                        var configurationDialogView = new ConfigurationDialog({
                            model: model,
                            collection: mainView.collection
                        });
                        configurationDialogView.show();
                    }, this),
                    error: function(request) {
                        alert(request.responseText);
                    }
                });
            },
            deleteSpaceVariable: function(e) {
                e.preventDefault();
                var spaceVariableId = $(e.currentTarget).parents('tr').data('id');
                var spaceVariable = this.collection.get(spaceVariableId);
                $.ajax({
                    url: AJS.contextPath() + '/rest/confluence-utils/1.0/spacevariable/' + spaceVariableId,
                    type: 'DELETE',
                    error: $.proxy(function(xhr) {
                        alert(xhr.responseText || 'Internal error');
                    }, this),
                    success: $.proxy(function() {
                        this.collection.remove(spaceVariable);
                        this._rebuildSpaceVariableList();
                    }, this)
                });

            },
            _rebuildSpaceVariableList: function() {
                mainView.startLoadingSpaceVariables();

                var htmlSpaceVariables = '';
                mainView.collection.each(function(spaceVariable) {
                    htmlSpaceVariables += Confluence.Templates.ConfluenceUtils.spaceVariableEntry({
                        spaceVariable: spaceVariable.toJSON()
                    });
                });
                $('#space-variables').empty().append(htmlSpaceVariables);

                mainView.finishLoadingSpaceVariables();
            }
        });

        var mainView = new MainView({collection: spaceVariableCollection});

        /* Fetch data */
        spaceVariableCollection.fetch({
        });
    });
});