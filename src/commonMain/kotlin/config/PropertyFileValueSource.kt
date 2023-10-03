package config

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.InvalidFileFormat
import com.github.ajalt.clikt.parameters.options.Option
import com.github.ajalt.clikt.sources.ValueSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

class PropertyFileValueSource(
    private val map: Map<String, String>,
) : ValueSource {
    override fun getValues(
        context: Context,
        option: Option,
    ): List<ValueSource.Invocation> {
        val aux =
            option.valueSourceKey
                ?: (context.commandNameWithParents().drop(1) + ValueSource.name(option)).joinToString(".")
        return if (map.containsKey(aux)) {
            listOf(ValueSource.Invocation.value(map[aux]))
        } else {
            emptyList()
        }
    }

    companion object {
        fun from(fileName: String): PropertyFileValueSource {
            val path = fileName.toPath()
            val map =
                try {
                    val lines =
                        FileSystem.SYSTEM.source(path).use { source ->
                            source.buffer().use { buffer ->
                                val stringList = mutableListOf<String>()
                                while (true) {
                                    val line = buffer.readUtf8Line() ?: break
                                    stringList.add(line)
                                }
                                stringList
                            }
                        }

                    lines.associate { line ->
                        val split = line.split("=")
                        if (split.size != 2) {
                            throw InvalidFileFormat(fileName, "Expected key=value format")
                        }
                        split[0] to split[1]
                    }
                } catch (e: Exception) {
                    emptyMap()
                }
            return PropertyFileValueSource(map)
        }
    }
}
