package geotrellis.data

import java.nio.IntBuffer
import geotrellis._
import geotrellis.process._
import geotrellis.raster._
import geotrellis.util._

final class RasterReadState(raster:Raster,
                            val target:RasterExtent) extends ReadState {
  val data:Array[Int] =
    raster.toArray
  val rasterExtent:RasterExtent = raster.rasterExtent

  def getType = raster.rasterType

  private var pos:Int = 0
  private var sz:Int = 0
  def initSource(position:Int, size:Int) {
    pos = position
    sz = size
  }

  @inline def assignFromSource(sourceIndex:Int, dest:MutableRasterData, destIndex:Int) {
    dest(destIndex) = data.apply(sourceIndex + pos)
  }
}

object RasterReader {
  def read(raster:Raster, targetOpt:Option[RasterExtent]): Raster = {
    val target = targetOpt.getOrElse(raster.rasterExtent)

    val readState = new RasterReadState(raster, target)
    val raster2 = readState.loadRaster()
    readState.destroy()

    raster2
  }
}
