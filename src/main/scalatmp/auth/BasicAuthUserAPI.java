/**
  * Created by ars on 15/04/2017.
  */
class BasicAuthUserAPI(core: SolrCoreConfig, admin: (String, String)) {
  private val entryPoint = s"${core.coreUrl}/$EntryPoint"


  def createUser(user: BasicAuthUser): Unit = {

  }


  def createUsers(users: User*): Unit = {
//    users.map(_.username).toSet.size == users.size

    val func: (String, String) => String = (f: String, s: String) => ""//f ~ s

    val userObject = users
      .map(u => (u.username, u.password)).mkString(",") // TODO Error code
      //.reduce(func)

    val json = "set-user" -> userObject

    Http(entryPoint)
      .auth(admin._1, admin._2)

  }

  def deleteUsers(usernames: String*): Unit = {
    ("delete-user" -> usernames)
  }


  def setBlockUnknown(isBlock: Boolean): Unit = {
    val json = ("set-property" -> ("blockUnknown" -> isBlock))


  }





//  case class User(username: String, password: String)

}
