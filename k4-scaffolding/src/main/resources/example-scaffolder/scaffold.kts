import com.luggsoft.k4.scaffolding.nodes.rootDirectory

rootDirectory {
    addStaticFile("foo.txt")
    addStaticFile("bar.txt")

    addDirectory("nested1/nested2/nested3") {
        addStaticFile("qux.txt")

        addDirectory("nested4/nested5") {
            addStaticFile("zip.txt")
        }
    }
}
