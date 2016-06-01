define('utils/space-variables-configuration-dialog', ['jquery', 'underscore', 'backbone'], function($, _, Backbone) {
    return Backbone.View.extend({
        el: '#space-variables-configuration-dialog',
        events: {
            'click #space-variables-configuration-dialog-ok': '_submit',
            'click #space-variables-configuration-dialog-cancel': 'hide'
        },
        initialize: function() {
            this.dialog = AJS.dialog2('#space-variables-configuration-dialog');
            this.$okButton = this.$('#space-variables-configuration-dialog-ok');
            this.$cancelButton = this.$('#space-variables-configuration-dialog-cancel');

            this._fillForm();
            this.dialog.on('hide', $.proxy(this.destroy, this));
        },
        destroy: function() {
            this.stopListening();
            this.undelegateEvents();
            this.$('form').off();
            this.dialog.off();

            this.$okButton.removeAttr('disabled');
            this.$cancelButton.removeAttr('disabled');

            this.$('#space-variables-configuration-dialog-name').val('');
            this.$('#space-variables-configuration-dialog-description').val('');
            this.$('#space-variables-configuration-dialog-page').auiSelect2('data', '');
            this.$('div.error').text('').addClass('hidden');
            this.$('#space-variables-configuration-dialog-error-panel').text('').addClass('hidden');
        },
        show: function() {
            this.dialog.show();
        },
        hide: function() {
            this.dialog.hide();
        },
        _initPageField: function() {
            this.$('#space-variables-configuration-dialog-page').auiSelect2({
                placeholder: AJS.I18n.getText('page.word'),
                allowClear: true,
                ajax: {
                    url: AJS.contextPath() + '/rest/confluence-utils/1.0/spacevariable/pages?key=' + AJS.params.spaceKey,
                    dataType: 'json',
                    data: function(filter) {
                        return {
                            filter: filter
                        };
                    },
                    results: function(data) {
                        return {
                            results: data
                        };
                    },
                    cache: true
                },
                formatResult: function(page) {
                    return Confluence.Templates.ConfluenceUtils.confluencePageFormatResult({
                        page: page
                    });
                },
                formatSelection: function(page) {
                    return Confluence.Templates.ConfluenceUtils.confluencePageFormatSelection({
                        page: page
                    });
                }
            });
        },
        _fillForm: function() {
            if (this.model.id !== undefined) {
                this.$('.aui-dialog2-header-main').text(AJS.I18n.getText('ru.mail.confluence.plugins.utils.spacevariables.configuration.edit'));
                this.$okButton.text(AJS.I18n.getText('edit.name'));

                this.$('#space-variables-configuration-dialog-name').val(this.model.get('name'));
                this.$('#space-variables-configuration-dialog-description').val(this.model.get('description'));
                this._initPageField();
                var page = this.model.get('page');
                this.$('#space-variables-configuration-dialog-page').auiSelect2('data' , {id: page.id, title: page.title, url: page.url, spaceKey: page.spaceKey, spaceName: page.spaceName});
            } else {
                this.$('.aui-dialog2-header-main').text(AJS.I18n.getText('ru.mail.confluence.plugins.utils.spacevariables.configuration.create'));
                this.$okButton.text(AJS.I18n.getText('create.name'));

                this._initPageField();
            }
        },
        _submit: function(e) {
            e.preventDefault();

            this.$okButton.attr('disabled', 'disabled');
            this.$cancelButton.attr('disabled', 'disabled');
            this.model.save(this._serializeSpaceVariable(), {
                success: $.proxy(this._ajaxSuccessHandler, this),
                error: $.proxy(this._ajaxErrorHandler, this)
            });
        },
        _serializeSpaceVariable: function() {
            var name = this.$('#space-variables-configuration-dialog-name').val();
            var description = this.$('#space-variables-configuration-dialog-description').val();
            var page = this.$('#space-variables-configuration-dialog-page').auiSelect2('data');
            var spaceKey = AJS.params.spaceKey;
            return {
                name: name,
                page: page,
                description: description,
                spaceKey: spaceKey
            };
        },
        _ajaxSuccessHandler: function(model, response) {
            this.collection.add(response, {merge: true});
            this.model.trigger('change', this.model);
            this.$okButton.removeAttr('disabled');
            this.$cancelButton.removeAttr('disabled');
            this.hide();
        },
        _ajaxErrorHandler: function(model, response) {
            var field = response.getResponseHeader('X-Atlassian-Rest-Exception-Field');
            if (field) {
                this.$('#space-variables-configuration-dialog-' + field + '-error').removeClass('hidden').text(response.responseText);
                if (field == 'name')
                    this.$('#space-variables-configuration-dialog-' + field).focus();
                else if (field == 'code')
                    editor.focus();
            } else
                this.$('#space-variables-configuration-dialog-error-panel').removeClass('hidden').text(response.responseText);
            this.$okButton.removeAttr('disabled');
            this.$cancelButton.removeAttr('disabled');
        }
    });
});