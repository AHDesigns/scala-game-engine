package systems

import components.{Camera, CameraActive, Transform}

object CamaraSystem extends System {
  override def init(): Unit = {}

  override def update(timeDelta: Float): Unit = {
    for {
      cameraId <- getSingleton[CameraActive].activeCamera
      camera <- getComponent[Camera] {
        _.name == cameraId
      }
      transform <- camera.getSibling[Transform]
    } {
//      transform.position.add(Directions.Right.toVec)
    }
  }
}
