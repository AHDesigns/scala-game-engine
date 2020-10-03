package ecs

trait SingletonComponent extends Component {
  override val singleton = true
}
