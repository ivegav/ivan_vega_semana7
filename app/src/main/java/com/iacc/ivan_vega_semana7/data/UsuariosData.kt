package com.iacc.ivan_vega_semana7.data

class UsuariosData {


    var usuarios: MutableList<Usuario> = ArrayList()


    class Usuario(var nombre: String, var password: String)

    init {
        usuarios.add(Usuario("Ivan", "123456"))
        usuarios.add(Usuario("Vega", "123456"))
        usuarios.add(Usuario("usuario1", "123456"))
        usuarios.add(Usuario("usuario2", "123456"))
        usuarios.add(Usuario("usuario3", "123456"))
    }

    fun validarUsuario(nombre: String, password: String): Boolean {
        for (usuario in usuarios) {
            if (usuario.nombre == nombre && usuario.password == password) {
                return true
            }
        }
        return false
    }

}