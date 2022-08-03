package com.artevak.kasirpos

import com.google.firebase.database.core.utilities.TreeNode
import java.util.*

fun stabilityOfCompound(edges: Array<IntArray>): Int {
    var result = -404
    // Write your logic here
    var farthesLeft = 0
    var farthesRight = 0

    result = (farthesLeft + farthesRight) / 2
    // Logic ends here
    return result
}

fun getTree(array : Array<IntArray>) : Tree?{
    if (array.isEmpty()) return null
    val root = Tree(array[0][0])
    val q: Queue<Tree> = LinkedList()
    q.add(root)
    for (i in array.indices) {
        val node: Tree = q.peek() as Tree
        if (node.left == null) {
            node.left = Tree(array[i][1])
            q.add(node.left)
        } else if (node.right == null) {
            node.right = Tree(array[i][1])
            if (array[i][1] != null) q.add(node.right)
            q.remove()
        }
    }
    return root

}

class Tree(var index : Int){

    fun Tree(index : Int){
        this.index = index
    }

    var left : Tree? = null
    var right : Tree? = null

}


fun main(args: Array<String>) {
    // Input
    val sc = Scanner(System.`in`)
    val n = sc.nextInt()
    val edges = Array(n - 1) { IntArray(2) }
    for (i in 0 until n - 1) {
        edges[i][0] = sc.nextInt()
        edges[i][1] = sc.nextInt()
    }
    sc.close()
    // Output
    println(stabilityOfCompound(edges))
}