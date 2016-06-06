package ru.mail.confluence.plugins.utils.spacevariables;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import org.apache.commons.lang3.StringUtils;

public class SpaceVariableManager {
    private final ActiveObjects ao;

    public SpaceVariableManager(ActiveObjects ao) {
        this.ao = ao;
    }

    public SpaceVariable getVariable(final int id) {
        return ao.executeInTransaction(new TransactionCallback<SpaceVariable>() {
            @Override
            public SpaceVariable doInTransaction() {
                SpaceVariable variable = ao.get(SpaceVariable.class, id);
                if (variable == null)
                    throw new IllegalArgumentException(String.format("Variable is not found by id %s", id));
                return variable;
            }
        });
    }

    public SpaceVariable[] getVariables() {
        return ao.executeInTransaction(new TransactionCallback<SpaceVariable[]>() {
            @Override
            public SpaceVariable[] doInTransaction() {
                return ao.find(SpaceVariable.class);
            }
        });
    }

    public SpaceVariable[] searchVariables(final long spaceId, final String filter, final int limit) {
        return ao.executeInTransaction(new TransactionCallback<SpaceVariable[]>() {
            @Override
            public SpaceVariable[] doInTransaction() {
                Query query = StringUtils.isBlank(filter)
                        ? Query.select().where("SPACE_ID = ? AND DELETED = false", spaceId)
                        : Query.select().where("SPACE_ID = ? AND LOWER(NAME) LIKE LOWER(?) AND DELETED = false", spaceId, '%' + filter + '%');
                if (limit != 0)
                    query = query.limit(limit);
                return ao.find(SpaceVariable.class, query.order("NAME"));
            }
        });
    }

    public SpaceVariable createVariable(final String name, final long pageId, final String description, final long spaceId) {
        return ao.executeInTransaction(new TransactionCallback<SpaceVariable>() {
            @Override
            public SpaceVariable doInTransaction() {
                SpaceVariable variable = ao.create(SpaceVariable.class);
                variable.setName(name);
                variable.setPageId(pageId);
                variable.setDescription(description);
                variable.setSpaceId(spaceId);
                variable.setDeleted(false);
                variable.save();
                return variable;
            }
        });
    }

    public SpaceVariable updateVariable(final int id, final String name, final long pageId, final String description, final long spaceId) {
        return ao.executeInTransaction(new TransactionCallback<SpaceVariable>() {
            @Override
            public SpaceVariable doInTransaction() {
                SpaceVariable variable = getVariable(id);
                variable.setName(name);
                variable.setPageId(pageId);
                variable.setDescription(description);
                variable.setSpaceId(spaceId);
                variable.setDeleted(false);
                variable.save();
                return variable;
            }
        });
    }

    public void deleteVariable(final int id) {
        ao.executeInTransaction(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction() {
                SpaceVariable variable = getVariable(id);
                variable.setDeleted(true);
                variable.save();
                return null;
            }
        });
    }
}
