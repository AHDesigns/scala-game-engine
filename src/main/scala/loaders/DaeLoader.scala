package loaders

import logging.Logger

import scala.xml._

/**
  * might come back to this when I want an animation and color data
  * Dae format from blender notes
  * library_geometries contains vertex data
  * triangles \ p lists info about indices
  * triangles \ @material links to library_materials / @id, which in turn links to a library effect
  */

/**
  * Load dae model format
  */
trait DaeLoader extends Logger {
  def load(file: String) = {
    val xml = XML.load(s"res/models/$file.dae")

    val data = xml \ "library_geometries"
    val triangles = data \ "geometry" \ "mesh" \ "triangles"
    val mesh = data \ "geometry" \ "mesh"
    val triangleInputs = triangles \ "input"
    val positionOffset = triangleInputs.size
    val positions = getFirst(triangles \ "p")
    val getTriangleInput = partiallyGetTriangleInput(triangleInputs)

    //    for {
    //      target <- List("VERTEX", "NORMAL")
    //      input <- getTriangleInput(target)
    //      info <- getGeometryInfo(mesh, input)
    //    } yield info
  }

  private def getGeometryInfo(mesh: NodeSeq, input: Input): Option[Int] = {
    Some(1)
  }

  private def partiallyGetTriangleInput(
      inputs: NodeSeq
  ): String => Either[String, Input] =
    (target: String) => {
      val input = inputs.find(where("semantic", target))

      val inputValues = for {
        src <- input.flatMap(_.attribute("source"))
        offset <- input.flatMap(_.attribute("offset"))
      } yield (src, offset)

      inputValues match {
        case Some((src, offset)) => Right(new Input(src.text, offset.text))
        case None                => Left(s"could not get Input for $target")
      }
    }

  private def where(attribute: String, is: String)(node: Node): Boolean =
    node.attribute(attribute).getOrElse("").toString == is

  private def getFirst(data: NodeSeq): Option[String] =
    data map (_.text) match {
      case Nil       => None
      case head :: _ => Some(head)
    }

  class Input(val source: String, _offset: String) {
    val offset: Int =
      try {
        _offset.toInt
      } catch {
        case _: Throwable =>
          logErr(
            s"Malformed dae file! input / offset value '${_offset}'could not be parsed to Int"
          )
          System.exit(1)
          0
      }
  }

}
