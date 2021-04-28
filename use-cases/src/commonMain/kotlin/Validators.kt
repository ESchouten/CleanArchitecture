fun email(email: String) = email.contains('@')
fun password(password: String) =
    password.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"))
