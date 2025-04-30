package com.frogcrew.frogcrew_backend.crewmember;

import com.frogcrew.frogcrew_backend.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Transactional
public class CrewListService {
    private UserRepository userRepository;
    private CrewListRepository crewListRepository;

    public CrewListService(UserRepository userRepository, CrewListRepository crewListRepository) {
        this.userRepository = userRepository;
        this.crewListRepository = crewListRepository;
    }

    public List<CrewList> findAll(){
        return this.crewListRepository.findAll();
        }

        public CrewList findById(Integer id){
            return this.crewListRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("CrewList", id));
        }
        public CrewList save(CrewList crewList){
            return this.crewListRepository.save(crewList);
        }

        public void deleteById(Integer id){
          this.crewListRepository.deleteById(id);
        }

        public void deleteAll(){
            this.crewListRepository.deleteAll();
        }

        public CrewList findByGameId(Integer Id){
            return this.crewListRepository.findByGameId(Id).orElseThrow(()-> new ObjectNotFoundException("CrewList","GameId" Id));
        }

        public List<CrewList> findByGameVenue(String venue){
        return this.crewListRepository.findByVenue(venue);
        }
        public List<CrewList> findByGameOpponent(String opponent){
        return this.crewListRepository.findByOpponent(opponent);
        }

        public List<CrewList> findByGameStart(String time)

        {
            return this.crewListRepository.findByGameStart(time);
        }
        public List<CrewList> findByGameDate(String date){
        return this.crewListRepository.findByGameDate(date);
        }


    }


