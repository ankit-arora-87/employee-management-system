package com.ems.userservice.repository.implementations;

import com.ems.userservice.model.User;
import com.ems.userservice.repository.UserRepoInterface;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;


public class UserRepositoryImpl {

//    @PersistenceContext
    @Autowired
    private EntityManager em;


//    @Override
//    public List<Match> getMatchesForATeamWithFilters(String name, int year, String againstTeam, String matchResult) {
//        System.out.println("R-Name: " + name);
//        System.out.println("R-Year: " + year);
//        System.out.println("R-Against Team: " + againstTeam);
//        System.out.println("R-Match Result: " + matchResult);
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Match> cq = cb.createQuery(Match.class);
//
//        Root<Match> match = cq.from(Match.class);
//
//        Predicate finalPredicate;
//
//        Predicate predicateForTeam1
//                = cb.equal(match.get("team1"), name);
//        Predicate predicateForTeam2
//                = cb.equal(match.get("team2"), name);
//        Predicate predicateForTeam
//                = cb.or(predicateForTeam1, predicateForTeam2);
//
//        finalPredicate = predicateForTeam;
//        System.out.println("Team Name Predicate: " + finalPredicate);
//
////        Predicate namePredicate = cb.
//        if(year == -1) year = 2020;
//
//        LocalDate startDate = LocalDate.of(year, 1, 1);
//        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
//
//        Predicate yearPredicate = cb.between(match.get("date"), startDate, endDate);
//        finalPredicate = cb.and(finalPredicate, yearPredicate);
//
//        System.out.println("Year Predicate: " +finalPredicate);
//
//        if(!againstTeam.equalsIgnoreCase("all")){
//            Predicate predicateForTeam1AgainstTeam
//                    = cb.equal(match.get("team2"), againstTeam);
//            Predicate predicateForTeam2AgainstTeam
//                    = cb.equal(match.get("team1"), againstTeam);
//            Predicate predicateForAgainstTeam
//                    = cb.or(cb.and(predicateForTeam1, predicateForTeam1AgainstTeam), cb.and(predicateForTeam2, predicateForTeam2AgainstTeam));
//            finalPredicate = cb.and(finalPredicate, predicateForAgainstTeam);
//            System.out.println("Against Team Predicate: " +finalPredicate);
//
//        }
//        System.out.println();
//        if(matchResult.equalsIgnoreCase("won") || matchResult.equalsIgnoreCase("lost")){
//            Predicate matchResultPredicate = null;
//            if(matchResult.equalsIgnoreCase("won")){
//                matchResultPredicate = cb.equal(match.get("winner"), name);
//            } else  if(matchResult.equalsIgnoreCase("lost")){
//                matchResultPredicate = cb.notEqual(match.get("winner"), name);
//            }
//            if(matchResultPredicate != null){
//                finalPredicate = cb.and(finalPredicate, matchResultPredicate);
//                System.out.println("Match Result Predicate: " +finalPredicate);
//            }
//        }
//
//        cq.where(finalPredicate);
//        TypedQuery<Match> query = em.createQuery(cq);
//
//        return query.getResultList();
//    }
}
