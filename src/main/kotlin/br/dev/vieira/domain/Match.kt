package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Match(
    val id: Long,
    val group: GroupFD?,
    val matchDay: Int?,
    val stage: StageFD,
    val homeTeam: Team?,
    val awayTeam: Team?,
    var homeTeamScore: Int?,
    var awayTeamScore: Int?,
    val dateTime: OffsetDateTime,
    val status: MatchStatusFD
) {
    fun isUpToDate(
        homeTeamScore: Int?,
        awayTeamScore: Int?,
        status: MatchStatusFD,
        dateTime: OffsetDateTime,
        group: GroupFD?,
        matchDay: Int?,
        stage: StageFD?
    ): Boolean {
        if(homeTeamScore != this.homeTeamScore)
            println("home team score is different: $homeTeamScore != ${this.homeTeamScore}")
        if(awayTeamScore!= this.awayTeamScore)
            println("away team score is different: $awayTeamScore != ${this.awayTeamScore}")
        if(status != this.status)
            println("status is different: $status != ${this.status}")
        if(dateTime != this.dateTime)
            println("dateTime is different: $dateTime != ${this.dateTime}")
        if(group != this.group)
            println("group is different: $group != ${this.group}")
        if(matchDay != this.matchDay)
            println("match day is different: $matchDay != ${this.matchDay}")
        if(stage != this.stage)
            println("stage is different: $stage != ${this.stage}")

        return homeTeamScore == this.homeTeamScore && awayTeamScore == this.awayTeamScore && status == this.status &&
                dateTime == this.dateTime && group == this.group && matchDay == this.matchDay && stage == this.stage
    }
}
