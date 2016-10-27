package com.gluonhq.sqlite

import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import com.gluonhq.sqlite._

object DBUtils {

    def copyDatabase(pathIni: String, pathEnd: String, name: String): Unit = {

        using(classOf[GluonSQLite].getResourceAsStream(pathIni + name)) { in =>
            using(new FileOutputStream(s"$pathEnd/$name")) { out =>
                val buffer = new Array[Byte](1024)
                Stream.continually(in.read(buffer)).takeWhile(_ != -1).foreach(out.write(buffer, 0, _))
                out.flush()
            }
        }

    }

}
