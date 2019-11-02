package pl.kopsoft.pczplan.core.group


import pl.kopsoft.pczplan.models.Group

interface GetGroupsListener {
    fun onGroupsGet(groups: List<Group>)
}
