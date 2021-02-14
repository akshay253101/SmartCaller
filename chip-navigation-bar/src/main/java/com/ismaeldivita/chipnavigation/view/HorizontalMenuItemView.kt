package com.ismaeldivita.chipnavigation.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.ismaeldivita.chipnavigation.R
import com.ismaeldivita.chipnavigation.model.MenuItem
import com.ismaeldivita.chipnavigation.util.setColorStateListAnimator
import com.ismaeldivita.chipnavigation.util.setCustomRipple

internal class HorizontalMenuItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MenuItemView(context, attrs) {

    private val title by lazy { findViewById<TextView>(R.id.cbn_item_title) }
    private val icon by lazy { findViewById<BadgeImageView>(R.id.cnb_item_icon) }
    private val container by lazy { findViewById<FrameLayout>(R.id.cbn_item_frame) }
    private lateinit var mask: Drawable

    init {
        View.inflate(getContext(), R.layout.cnb_horizontal_menu_item, this)
        layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
    }

    override fun bind(item: MenuItem) {
        id = item.id

        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        contentDescription = item.contentDescription ?: item.title

        isEnabled = item.enabled
        item.menuStyle.textAppearance?.let { TextViewCompat.setTextAppearance(title, it) }
        title.text = item.title
        title.setTextColor(item.textColor)
        title.setColorStateListAnimator(
            color = item.textColor,
            unselectedColor = item.menuStyle.unselectedColor
        )

        icon.layoutParams.width = item.menuStyle.iconSize
        icon.layoutParams.height = item.menuStyle.iconSize
        icon.setImageResource(item.icon)
        icon.setBadgeColor(item.menuStyle.badgeColor)
        icon.setColorStateListAnimator(
            color = item.iconColor,
            unselectedColor = item.menuStyle.unselectedColor,
            mode = item.tintMode
        )

        val containerBackground = GradientDrawable().apply {
            cornerRadius = item.menuStyle.radius
            setColor(item.backgroundColor)
        }

        mask = GradientDrawable().apply {
            cornerRadius = item.menuStyle.radius
            setColor(Color.BLACK)
        }

        DrawableCompat.setTint(mask, Color.BLACK)
        container.setCustomRipple(containerBackground, mask)
    }

    override fun showBadge(count: Int) {
        icon.showBadge(count)
    }

    override fun dismissBadge() {
        icon.dismissBadge()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        if (!enabled && isSelected) {
            isSelected = false
        }
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        if (selected) {
            /** Hack to fix the ripple issue before a scene transition on SDKs < P */
            container.visibility = View.GONE
            mask.jumpToCurrentState()
            container.visibility = View.VISIBLE
            title.visibility = View.VISIBLE
        } else {
            title.visibility = View.GONE
        }
    }

}