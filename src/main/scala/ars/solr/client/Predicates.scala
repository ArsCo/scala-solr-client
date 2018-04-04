package ars.solr.client

import ars.precondition.RequireUtils.requireNotNull
import ars.solr.client.mapping.SolrMappingException
import com.typesafe.scalalogging.Logger

import scala.reflect.ClassTag
import scala.util.Try

/** Default predicates factory.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.5
  */
object Predicates {

  private val abortFunction = (s: String) => throw new SolrMappingException(s)
  private val discardFailedFunction = logger.error(_)

  /** Creates new instance of action predicate. Action predicate calls `action` function
    * if it's argument is `Failure(_)`.
    *
    * @param action the action function
    *
    * @tparam Result the result type
    *
    * @return new instance of action predicate
    */
  def newActionPredicate[Result](
    action: PredicateFailureAction,
    formatter: ErrorMessageFormatter[Result] = defaultFormatter _
  )(implicit classTag: ClassTag[Result]): Predicate[Result] = {
    actionPredicate(_, action, formatter)
  }

  /** Creates new instance of abort predicate. Abort predicate throws new [[SolrMappingException]]
    * if it's argument is `Failure(_)`.
    *
    * @tparam Result the result type
    *
    * @return new instance of abort predicate
    */
  def newAbortPredicate[Result](
    formatter: ErrorMessageFormatter[Result] = defaultFormatter _
  )(implicit classTag: ClassTag[Result]): Predicate[Result] = {
    newActionPredicate(abortFunction, formatter)
  }

  /** Creates new instance of discard failed predicate. Discard failed predicate logs the error message
    * if it's argument `Failure(_)`
    *
    * @tparam Result the result type
    *
    * @return new instance of discard failed predicate
    */
  def newDiscardFailedPredicate[Result](
    formatter: ErrorMessageFormatter[Result] = defaultFormatter _
  )(implicit classTag: ClassTag[Result]): Predicate[Result] = {
    newActionPredicate(discardFailedFunction, formatter)
  }

  private def actionPredicate[Result](tryValue: Try[Result],
                                      action: PredicateFailureAction,
                                      formatter: ErrorMessageFormatter[Result])
                                     (implicit classTag: ClassTag[Result]): Boolean = {
    requireNotNull(tryValue, "tryValue")
    requireNotNull(action, "action")
    requireNotNull(formatter, "formatter")

    if (tryValue.isFailure) {
      action(defaultFormatter(tryValue))
    }
    tryValue.isSuccess
  }

  private def defaultFormatter[Result](tryValue: Try[Result])(implicit classTag: ClassTag[Result]): String = {
    val className = classTag.runtimeClass.getName
    s"Conversion error: $tryValue to type of $className"
  }

  private lazy val logger = Logger[DefaultQueryResult]
}
