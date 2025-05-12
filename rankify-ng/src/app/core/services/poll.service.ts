import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, delay } from 'rxjs/operators';
import { Poll, PollOption } from '../models/poll.model';

@Injectable({
  providedIn: 'root'
})
export class PollService {
  private apiUrl = '/api/polls'; // Backend API URL for future implementation
  
  // Mock data for testing without backend
  private mockPolls: Poll[] = [
    {
      id: '1',
      name: 'Team Lunch Location',
      isOpen: true,
      options: [
        { id: '1-1', name: 'Italian Restaurant' },
        { id: '1-2', name: 'Sushi Bar' },
        { id: '1-3', name: 'Burger Joint' },
        { id: '1-4', name: 'Salad Place' }
      ],
      createdAt: new Date('2025-04-15')
    },
    {
      id: '2',
      name: 'Next Sprint Focus',
      isOpen: false,
      options: [
        { id: '2-1', name: 'Performance Optimization' },
        { id: '2-2', name: 'New Features' },
        { id: '2-3', name: 'Bug Fixes' },
        { id: '2-4', name: 'Documentation' }
      ],
      createdAt: new Date('2025-04-10')
    }
  ];

  constructor(private http: HttpClient) {}

  /**
   * Creates a new poll
   * @param poll The poll to create
   * @returns Observable with the created poll
   */
  createPoll(poll: Poll): Observable<Poll> {
    // MOCK IMPLEMENTATION
    // In a real implementation, replace with: 
    // return this.http.post<Poll>(this.apiUrl, poll).pipe(...)
    
    const newPoll: Poll = {
      ...poll,
      id: Date.now().toString(),
      createdAt: new Date(),
      options: poll.options.map((option, index) => ({
        ...option,
        id: `${Date.now()}-${index}`
      }))
    };
    
    this.mockPolls.push(newPoll);
    return of(newPoll).pipe(delay(500)); // Simulate network delay
  }

  /**
   * Retrieves a poll by its name
   * @param pollName The name of the poll to retrieve
   * @returns Observable with the poll data
   */
  getPollByName(pollName: string): Observable<Poll> {
    // MOCK IMPLEMENTATION
    // In a real implementation, replace with: 
    // return this.http.get<Poll>(`${this.apiUrl}/name/${pollName}`).pipe(...)
    
    const poll = this.mockPolls.find(p => p.name === pollName);
    
    if (poll) {
      return of({...poll}).pipe(delay(500)); // Return a copy to prevent unintended modifications
    }
    
    return throwError(() => new Error('Poll not found'));
  }

  /**
   * Starts a poll
   * @param pollId The ID of the poll to start
   * @returns Observable with the updated poll
   */
  startPoll(pollId: string): Observable<Poll> {
    // MOCK IMPLEMENTATION
    // In a real implementation, replace with: 
    // return this.http.patch<Poll>(`${this.apiUrl}/${pollId}/start`, {}).pipe(...)
    
    const pollIndex = this.mockPolls.findIndex(p => p.id === pollId);
    
    if (pollIndex !== -1) {
      this.mockPolls[pollIndex].isOpen = true;
      return of({...this.mockPolls[pollIndex]}).pipe(delay(500));
    }
    
    return throwError(() => new Error('Poll not found'));
  }

  /**
   * Stops a poll
   * @param pollId The ID of the poll to stop
   * @returns Observable with the updated poll
   */
  stopPoll(pollId: string): Observable<Poll> {
    // MOCK IMPLEMENTATION
    // In a real implementation, replace with: 
    // return this.http.patch<Poll>(`${this.apiUrl}/${pollId}/stop`, {}).pipe(...)
    
    const pollIndex = this.mockPolls.findIndex(p => p.id === pollId);
    
    if (pollIndex !== -1) {
      this.mockPolls[pollIndex].isOpen = false;
      return of({...this.mockPolls[pollIndex]}).pipe(delay(500));
    }
    
    return throwError(() => new Error('Poll not found'));
  }

  /**
   * Gets all available polls
   * @returns Observable with the list of polls
   */
  getAllPolls(): Observable<Poll[]> {
    // MOCK IMPLEMENTATION
    // In a real implementation, replace with: 
    // return this.http.get<Poll[]>(this.apiUrl).pipe(...)
    
    return of([...this.mockPolls]).pipe(delay(500));
  }
}
