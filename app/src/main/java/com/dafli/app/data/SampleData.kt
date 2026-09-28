package com.dafli.app.data

import com.dafli.app.theme.DColor

/**
 * Fictional sample catalogue — every artist, song and label here is invented for the
 * Dafli case study. Replace with your backend.
 */
object Sample {
    // ---- Tracks -------------------------------------------------------------
    val noor = Track("noor", "Noor", "Sufi Circle", Art.J, 261, album = "Noor")
    val marigoldHours = Track("mh", "Marigold Hours", "Kabir Sen", Art.A, 228, album = "Marigold Hours")
    val studioNights = Track("sn", "Studio Nights", "Nila Raghavan", Art.B, 244)
    val paadalNights = Track("pn", "Paadal Nights", "Nila Raghavan", Art.K, 236)
    val bassWala = Track("bwp", "Bass Wala Pyaar", "DJ Rudra", Art.I, 198, explicit = true)
    val neonBandra = Track("nb", "Neon Bandra", "The Sea Link", Art.C, 214)
    val dholRepublic = Track("dr", "Dhol Republic", "Dhol Republic", Art.D, 207)
    val chaiRain = Track("cr", "Chai & Rain", "Kabir Sen, Ira Menon", Art.E, 242, explicit = true, album = "Marigold Hours")
    val gullyMein = Track("gm", "Gully Mein", "MC Veer", Art.HipHop, 186, explicit = true)
    val localTrain = Track("lt", "Local Train", "Kabir Sen", Art.Hero, 195, album = "Kabira Nights")
    val ghatKiSubah = Track("gks", "Ghat Ki Subah", "Kabir Sen", Art.H, 310, album = "Morning Calm")
    val rooftopNights = Track("rn", "Rooftop Nights", "Rooftop Radio", Art.Indie, 221)
    val kabira = Track("kb", "Kabira", "Kabir Sen", Art.KabirSen, 233, album = "Kabira Nights")
    val sarsonNights = Track("sar", "Sarson Nights", "Gurnoor", Art.Punjabi, 202)
    val chalHun = Track("ch", "Chal Hun", "Gurnoor, MC Veer", Art.Workout, 190)
    val baarish = Track("bks", "Baarish Ki Shaam", "Old Bombay Orchestra", Art.Retro, 245)
    val shamDhale = Track("sd", "Sham Dhale", "Irfan Ali", Art.Devotional, 262)

    val allTracks = listOf(noor, marigoldHours, studioNights, paadalNights, bassWala, neonBandra, dholRepublic, chaiRain, gullyMein, localTrain, ghatKiSubah, rooftopNights, kabira)

    // ---- Artists ------------------------------------------------------------
    val kabirSen = Artist("Kabir Sen", Art.KabirSen, "Artist · Hindi indie pop · Mumbai")
    val iraMenon = Artist("Ira Menon", Art.IraMenon)
    val nilaRaghavan = Artist("Nila Raghavan", Art.B)
    val aravKapoor = Artist("Arav Kapoor", Art.AravKapoor)
    val rooftopRadio = Artist("Rooftop Radio", Art.Indie)
    val mcVeer = Artist("MC Veer", Art.HipHop)
    val artists = listOf(kabirSen, iraMenon, nilaRaghavan, aravKapoor)

    // ---- Collections --------------------------------------------------------
    val localTrainNights = Collection("ltn", "Local Train Nights", "Made for Vishal · 42 songs", Art.Hero, Kind.Mix)
    val dailyMix1 = Collection("dm1", "Daily Mix 1", "Ira Menon, Kabir Sen, Anu Das and more", Art.F, Kind.Mix)
    val dailyMix2 = Collection("dm2", "Daily Mix 2", "The Sea Link, Rooftop Radio, Nila", Art.G, Kind.Mix)
    val morningCalm = Collection("mc", "Morning Calm", "Soft classical and devotional", Art.H, Kind.Mix)
    val roadTrip = Collection("rt", "Road Trip 2026", "Playlist · Aisha · collaborative", Art.C, Kind.Playlist)
    val marigoldAlbum = Collection("mha", "Marigold Hours", "Album · Kabir Sen", Art.A, Kind.Album)

    val recentlyPlayed = listOf(
        Collection("r1", "Marigold Hours", "Kabir Sen", Art.A, Kind.Album),
        Collection("r2", "Studio Nights", "Nila Raghavan", Art.B, Kind.Album),
        Collection("r3", "Neon Bandra", "The Sea Link", Art.C, Kind.Album),
        Collection("r4", "Dhol Republic", "Dhol Republic", Art.D, Kind.Album),
    )
    val madeForYou = listOf(dailyMix1, dailyMix2, morningCalm)

    val madeForYouAll = listOf(
        Collection("a1", "Daily Mix 1", "Ira Menon, Kabir Sen and more", Art.F, Kind.Mix),
        Collection("a2", "Daily Mix 2", "The Sea Link, Rooftop Radio", Art.G, Kind.Mix),
        Collection("a3", "Morning Calm", "Soft classical, devotional", Art.H, Kind.Mix),
        Collection("a4", "Desi Bass Mix", "DJ Rudra, MC Veer", Art.I, Kind.Mix),
        Collection("a5", "Rainy Day Lo-fi", "Chai & Rain and more", Art.LoFi, Kind.Mix),
        Collection("a6", "Night Drive", "Neon Bandra and more", Art.C, Kind.Mix),
        Collection("a7", "Evening Bhajans", "Calm devotional", Art.Devotional, Kind.Mix),
        Collection("a8", "Your 90s Mix", "Retro hits you replay", Art.Retro, Kind.Mix),
    )

    data class Mood(val name: String, val sub: String, val art: Art)
    val moods = listOf(
        Mood("Chill", "Slow evenings", Art.LoFi),
        Mood("Workout", "High BPM", Art.Workout),
        Mood("Devotional", "Bhajan, sufi, kirtan", Art.Devotional),
        Mood("Party", "Desi bass, Punjabi", Art.I),
    )

    data class Lang(val script: String, val name: String)
    val languages = listOf(
        Lang("हिन्दी", "Hindi"), Lang("ਪੰਜਾਬੀ", "Punjabi"), Lang("தமிழ்", "Tamil"),
        Lang("తెలుగు", "Telugu"), Lang("বাংলা", "Bengali"), Lang("മലയാളം", "Malayalam"),
    )

    data class Genre(val name: String, val art: Art)
    val browseAll = listOf(
        Genre("Bollywood", Art.Bollywood), Genre("Punjabi", Art.Punjabi),
        Genre("Desi Hip-Hop", Art.HipHop), Genre("Lo-fi", Art.LoFi),
        Genre("Devotional", Art.Devotional), Genre("Indie", Art.Indie),
        Genre("Workout", Art.Workout), Genre("Retro 90s", Art.Retro),
    )

    val lyrics = listOf(
        "Raat ki roshni mein", "hum dono chal pade", "laltein si jalti hai", "teri baatein yahan",
        "dil ki galiyon mein", "ek dhun si baaki hai", "noor hai tu, noor hai tu", "sham se subah tak", "chalta rahe ye safar",
    )

    // ---- People -------------------------------------------------------------
    val me = Person("Vishal Kushwaha", "@vishal.k", "V", DColor.AvatarV)
    val aisha = Person("Aisha", "@aisha.writes", "A", DColor.AvatarA)
    val meera = Person("Meera", "@meera", "M", DColor.AvatarM)
    val dev = Person("Dev", "@dev", "D", DColor.AvatarD)
    val kiran = Person("Kiran", "@kiran", "K", DColor.AvatarK)
    const val email = "vishal.k@gmail.com"
}
