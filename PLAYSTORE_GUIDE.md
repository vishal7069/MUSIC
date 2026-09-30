# Dafli ko Play Store par live karne ki guide

Ye poori list order mein hai. ✅ = maine kar diya, 👤 = aapko karna hai (payment, ID, login sirf aap kar sakte ho).

---

## 1. App ready ✅
- Hindi music **unofficial JioSaavn community API** se aata hai. Koi API key nahi chahiye; service availability badal sakti hai.
- Background mein music chalta hai, aur notification / lock screen se control hota hai (Media3 ExoPlayer).
- Liked songs, history aur taste sirf phone par save hote hain. Koi server ya database nahi chahiye.
- **Android 16 (API 36)** target kiya hai, jo Google ka 2026 wala rule hai.
- Jo features abhi backend ke bina fake hote (login, chat, Listen Together, downloads), wo hide kar diye hain. Play Store fake features ko reject kar deta hai.

## 2. Android Studio install 👤 (main computer se madad karunga)
1. https://developer.android.com/studio se download karo, install karo, "Standard" setup chuno.
2. **File → Open →** `Desktop\Dafli` → Gradle sync hone do. Pehli baar 10–20 min lagte hain, internet chahiye.
3. Upar **Device Manager** mein ek Pixel emulator banao → ▶ **Run**. App chalna chahiye aur gaana bajna chahiye.

## 3. Signing key banana 👤 (ye file zindagi bhar sambhal ke rakhni hai)
1. Android Studio → **Build → Generate Signed App Bundle or APK → Android App Bundle → Next**.
2. **Create new…** → file ka naam `dafli-upload.jks` do, aur isse `Desktop\Dafli` ke **bahar** kisi safe jagah rakho.
3. Strong password do. Alias `dafli` rakho. Name mein apna naam bharo.
4. Ye **.jks file + dono passwords** Google Drive / pen-drive mein backup karo. Kho gaye to aage app update nahi kar paoge.
5. Finish → **release** chuno → `app/release/app-release.aab` ban jayegi. ✅ Yahi Play Store par upload hoti hai.

## 4. Privacy policy ka public link 👤 + ✅
- Page ready hai: `store/privacy.html`.
- Isse free mein host karna hoga: **Google Sites** (sites.google.com, usi Google account se) ya **GitHub Pages**. Main steps mein madad karunga.
- Jo link mile, wahi Play Console mein daalna hai. `AppConfig.kt` mein `PRIVACY_URL` bhi update karna hai.

## 5. Google Play Developer account 👤
1. https://play.google.com/console → Google account se sign in karo.
2. Account type: **Personal** (ya company ho to Organization, uske liye D-U-N-S number chahiye).
3. **$25 ek baar ki fees** do, aur ID verification karo (PAN / Aadhaar / passport + address). Isme 1–3 din lag sakte hain.
4. Phone number aur email verify karo.

## 6. App create karna 👤 (listing ka text main de chuka hoon: `store/LISTING.md`)
1. Play Console → **Create app** → Name: `Dafli: Free Music Player`, Language: English (India), App, Free → declarations tick karo.
2. **Store listing:** `store/LISTING.md` se text copy karo. Icon `store/icon-512.png` aur feature graphic `store/feature-graphic-1024x500.png` hai.
3. **Screenshots:** emulator mein app chalao → side toolbar ka 📷 → 4–8 screenshots (Home, Search, Now Playing, Artist, Library).
4. **App content:** saare jawab `store/LISTING.md` ki table mein hain.

## 7. Closed testing – 14 din 👤 (naye personal account ka zaroori rule)
1. **Testing → Closed testing → Create track** → `.aab` upload karo → release notes likho → Save → Review → Start rollout.
2. **Testers:** kam se kam **12 log** (dost/family), unke Gmail IDs ki email list banao.
3. Opt-in link unhe bhejo. Wo link se join karke Play Store se app install karein aur **14 din tak kabhi-kabhi use karein**.
4. 14 din baad Dashboard mein **Apply for production** → sawaalon ke jawab do (main likhne mein madad karunga).

## 8. Production 🚀
- Approval ke baad **Production → Create new release** → wahi `.aab` → Rollout.
- Google ka review aam taur par 1–7 din leta hai. Uske baad app Play Store par sabko dikhega.

---

### Update kaise dena hai (future)
`app/build.gradle.kts` mein `versionCode` +1 karo aur `versionName` badlo → nayi signed `.aab` banao → Production mein upload karo.

### Zaroori note
- Community API official JioSaavn service nahi hai. Public/Play Store release se pehle music catalogue ke rights/permissions verify karo aur public privacy page ko JioSaavn, saavn.sumit.co aur saavncdn.com ke liye update karo.
- Agar koi galat ya copyright wala gaana mile, to app mein har gaane ke ⋮ menu mein **Report** option hai. Play Store ki UGC policy ke liye ye zaroori hai.
