package pl.kopsoft.pczplan.interfaces


import pl.kopsoft.pczplan.models.Group

interface GetGroupsListener {
    fun onGroupsGet(groups: ArrayList<Group>)
}
