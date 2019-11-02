package pl.kopsoft.pczplan.core.group;


import java.util.List;

import pl.kopsoft.pczplan.models.Group;

public interface GetGroupsListener {
    void OnGroupsGet(List<Group> groups);
}
