package ars.solr.client.request.common

/** Methods to switch on/off groups of request parameters for builders.
  *
  * @tparam Builder the builder type
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
trait Switchable[Builder] {

  /**
    * Alias for `switch(true)` method call.
    *
    * @return the `this` instance
    */
  def enable(): Builder = switch(true)

  /**
    * Alias for `switch(false)` method call.
    *
    * @return the `this` instance
    */
  def disable(): Builder = switch(false)

  /**
    * Switch on/off group of request parameters.
    *
    * @param isOn `true` to switch on and `false` to switch off group.
    * @return the `this` instance
    */
  def switch(isOn: Boolean): Builder
}
